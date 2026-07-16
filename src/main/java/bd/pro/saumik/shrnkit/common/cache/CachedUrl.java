package bd.pro.saumik.shrnkit.common.cache;

import java.time.Instant;
import java.util.UUID;

public record CachedUrl(

        UUID shortUrlId,
        String shortCode,
        String originalUrl,
        boolean active,
        Instant expiresAt

) {
    public boolean canRedirect() {
        return active && (expiresAt == null || expiresAt.isAfter(Instant.now()));
    }
}