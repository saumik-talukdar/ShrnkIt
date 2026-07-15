package bd.pro.saumik.shrnkit.domain.url.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UrlSummaryResponse(

        UUID id,

        String shortCode,

        String shortUrl,

        String originalUrl,

        boolean active,

        Instant expiresAt,

        Instant createdAt

) {}