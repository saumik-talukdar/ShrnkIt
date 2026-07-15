package bd.pro.saumik.shrnkit.domain.url.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateShortUrlRequest(

        @NotBlank(message = "Original URL is required.")
        @Size(max = 2048)
        String originalUrl,

        Instant expiresAt

) {}