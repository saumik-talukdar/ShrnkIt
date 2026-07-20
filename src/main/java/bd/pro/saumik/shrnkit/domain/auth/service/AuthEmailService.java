package bd.pro.saumik.shrnkit.domain.auth.service;

import bd.pro.saumik.shrnkit.common.mail.EmailMessage;
import bd.pro.saumik.shrnkit.common.mail.EmailService;
import bd.pro.saumik.shrnkit.common.mail.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;
    private final EmailTemplateService emailTemplateService;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendVerificationMail(UUID userId, String firstName, String email) {
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

    public void sendResetPasswordMail(UUID userId, String firstName, String email) {
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