package bd.pro.saumik.shrnkit.domain.url.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ShortUrlResponse(

        UUID id,

        String originalUrl,

        String shortCode,

        String shortUrl,

        Instant expiresAt,

        Instant createdAt

) {}