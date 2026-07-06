package bd.pro.saumik.shrnkit.domain.auth.service;

import bd.pro.saumik.shrnkit.common.exception.EmailAlreadyExistsException;
import bd.pro.saumik.shrnkit.common.exception.EmailNotVerifiedException;
import bd.pro.saumik.shrnkit.common.exception.UserNotFoundException;
import bd.pro.saumik.shrnkit.common.mail.EmailMessage;
import bd.pro.saumik.shrnkit.common.mail.EmailService;
import bd.pro.saumik.shrnkit.common.mail.EmailTemplateService;
import bd.pro.saumik.shrnkit.domain.auth.dto.request.LoginRequest;
import bd.pro.saumik.shrnkit.domain.auth.dto.request.RegisterRequest;
import bd.pro.saumik.shrnkit.domain.auth.dto.response.AuthResponse;
import bd.pro.saumik.shrnkit.domain.user.entity.User;
import bd.pro.saumik.shrnkit.domain.user.repository.UserRepository;
import bd.pro.saumik.shrnkit.security.JwtService;
import bd.pro.saumik.shrnkit.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final PasswordResetService passwordResetService;

    private final EmailVerificationService emailVerificationService;

    private final EmailService emailService;

    private final EmailTemplateService emailTemplateService;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiry;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exist!");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        userRepository.save(user);

        sendVerificationMail(user.getId(),user.getFirstName(),user.getEmail());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        // automatic handling in AppUserDetails
//        if (!user.isEmailVerified()) {
//            throw new EmailNotVerifiedException(
//                    "Please verify your email before logging in."
//            );
//        }

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                refreshTokenService.createRefreshToken(user.getId());

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                accessTokenExpiry/1000
        );
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {

        UUID userId =
                refreshTokenService.getUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        refreshTokenService.revokeRefreshToken(userId,refreshToken);

        String newRefresh =
                refreshTokenService.createRefreshToken(userId);

        String access =
                jwtService.generateAccessToken(user);

        return new AuthResponse(
                access,
                newRefresh,
                "Bearer",
                accessTokenExpiry/1000
        );
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            return; // silent
        }
        sendResetPasswordMail(user.getId(), user.getFirstName(), user.getEmail());
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        UUID userId = passwordResetService.validateResetToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        user.incrementPasswordVersion();

        refreshTokenService.revokeAllRefreshTokens(userId);

        passwordResetService.deleteResetToken(token);
    }

    @Transactional
    public void verifyEmail(String token) {
        UUID userId = emailVerificationService.getId(token);
        if(userId == null){
            return;
        }
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            return; // silent
        }
        user.verifyEmail();
        emailVerificationService.deleteToken(token);
    }

    public void resendVerificationEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if(user == null || user.isEmailVerified()) {
            return; // silent
        }
        sendVerificationMail(user.getId(),user.getFirstName(),user.getEmail());
    }


    // utils

    private void sendVerificationMail(UUID userId,String firstName, String email) {
        String token = emailVerificationService.createToken(userId);
        String verificationUrl = frontendUrl + "/auth/verification/" + token;
        String html = emailTemplateService.verificationEmail(
                firstName,
                verificationUrl
        );
        emailService.sendEmail(
                new EmailMessage(
                        email,
                        "Verify your email",
                        html
                )
        );
    }

    private void sendResetPasswordMail(UUID userId,String firstName, String email) {
        String resetToken = passwordResetService.createResetToken(userId);
        String url = frontendUrl + "/auth/reset-password/" + resetToken;
        String html = emailTemplateService.resetPasswordEmail(
                firstName,
                url
        );
        emailService.sendEmail(
                new EmailMessage(
                        email,
                        "Verify your email",
                        html
                )
        );
    }
}