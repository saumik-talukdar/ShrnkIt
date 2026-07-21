package bd.pro.saumik.shrnkit.domain.analytics.listener;

import bd.pro.saumik.shrnkit.common.http.RefererInfo;
import bd.pro.saumik.shrnkit.common.http.RefererService;
import bd.pro.saumik.shrnkit.common.http.UserAgentInfo;
import bd.pro.saumik.shrnkit.common.http.UserAgentService;
import bd.pro.saumik.shrnkit.domain.analytics.event.UrlVisitedEvent;
import bd.pro.saumik.shrnkit.domain.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsEventListener {

    private final AnalyticsService analyticsService;
    private final UserAgentService userAgentService;
    private final RefererService refererService;

    @Async("analyticsExecutor")
    @EventListener
    public void onUrlVisited(UrlVisitedEvent event) {

        UserAgentInfo userAgent =
                userAgentService.parse(
                        event.userAgent()
                );

        RefererInfo referer =
                refererService.parse(
                        event.referer()
                );

        analyticsService.recordClick(
                event.shortUrlId(),
                event.visitorId(),
                userAgent,
                referer
        );

    }
}