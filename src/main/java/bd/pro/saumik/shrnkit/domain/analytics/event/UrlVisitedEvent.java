package bd.pro.saumik.shrnkit.domain.analytics.event;

import java.util.UUID;

public record UrlVisitedEvent(
        UUID shortUrlId,
        UUID visitorId,
        String userAgent,
        String referer
) {}