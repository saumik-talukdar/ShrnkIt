package bd.pro.saumik.shrnkit.domain.analytics.listener;

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

    @Async("analyticsExecutor")
    @EventListener
    public void onUrlVisited(UrlVisitedEvent event) {

        analyticsService.recordClick(
                event.shortUrlId(),
                event.visitorId()
        );

    }
}