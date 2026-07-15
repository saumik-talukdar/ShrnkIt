package bd.pro.saumik.shrnkit.domain.url.dto.request;

import jakarta.validation.constraints.Size;

import java.time.Instant;

public record UpdateShortUrlRequest(

        @Size(max = 2048)
        String originalUrl,

        Instant expiresAt,

        Boolean active

) {}