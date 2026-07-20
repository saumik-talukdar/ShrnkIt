package bd.pro.saumik.shrnkit.domain.auth.event;

import java.util.UUID;

public record PasswordResetRequestedEvent(
        UUID userId,
        String firstName,
        String email
) {
}