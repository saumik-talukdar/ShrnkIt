package bd.pro.saumik.shrnkit.domain.analytics.service;

import bd.pro.saumik.shrnkit.common.exception.ShortUrlNotFoundException;
import bd.pro.saumik.shrnkit.common.http.UserAgentInfo;
import bd.pro.saumik.shrnkit.domain.analytics.dto.response.AnalyticsResponse;
import bd.pro.saumik.shrnkit.domain.analytics.entity.ClickEvent;
import bd.pro.saumik.shrnkit.domain.analytics.repository.ClickEventRepository;
import bd.pro.saumik.shrnkit.domain.url.entity.ShortUrl;
import bd.pro.saumik.shrnkit.domain.url.service.UrlOwnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ClickEventRepository clickEventRepository;

    private final UrlOwnershipService urlOwnershipService;

    @Transactional
    public void recordClick(
            UUID shortUrlId,
            UUID visitorId,
            UserAgentInfo info
    ) {

        // System.out.println("visitorId = " + visitorId);

        clickEventRepository.save(
                ClickEvent.builder()
                        .shortUrlId(shortUrlId)
                        .visitorId(visitorId)
                        .browser(info.browser())
                        .operatingSystem(info.operatingSystem())
                        .deviceType(info.deviceType())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public AnalyticsResponse getAnalytics(
            UUID userId,
            UUID urlId
    ) {

        urlOwnershipService.getOwnedUrl(userId, urlId);

        return new AnalyticsResponse(
                clickEventRepository.countByShortUrlId(urlId),
                clickEventRepository.countUniqueVisitors(urlId)
        );
    }

}