package bd.pro.saumik.shrnkit.domain.user.service;

import bd.pro.saumik.shrnkit.common.exception.BadRequestException;
import bd.pro.saumik.shrnkit.domain.user.entity.User;
import bd.pro.saumik.shrnkit.domain.user.repository.UserRepository;
import bd.pro.saumik.shrnkit.security.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(User user, String oldPassword, String newPassword) {

        if(!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BadRequestException("Password does not match old password");
        }
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new BadRequestException(
                    "New password must be different from current password."
            );
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.incrementPasswordVersion();
        logoutAll(user.getId());
    }

    public void logout(UUID userId, String refreshToken) {
        refreshTokenService.revokeRefreshToken(userId,refreshToken);
    }

    public void logoutAll(UUID userId) {
        refreshTokenService.revokeAllRefreshTokens(userId);
    }
}
