package bd.pro.saumik.shrnkit.domain.auth.event;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String firstName,
        String email
) {
}