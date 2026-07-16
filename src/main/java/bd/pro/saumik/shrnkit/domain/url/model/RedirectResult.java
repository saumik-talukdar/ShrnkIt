package bd.pro.saumik.shrnkit.domain.url.model;

import java.util.UUID;

public record RedirectResult(

        UUID shortUrlId,

        String originalUrl

) {}