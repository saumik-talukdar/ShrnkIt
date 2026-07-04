package bd.pro.saumik.shrnkit.domain.auth.service;

import bd.pro.saumik.shrnkit.common.exception.EmailAlreadyExistsException;
import bd.pro.saumik.shrnkit.common.exception.UserNotFoundException;
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

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiry;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exist!");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(
                        passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        userRepository.save(user);

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
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new UserNotFoundException("User not found"));

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

        refreshTokenService.revokeRefreshToken(refreshToken);

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
}