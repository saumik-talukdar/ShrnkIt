package bd.pro.saumik.shrnkit.domain.analytics.controller;

import bd.pro.saumik.shrnkit.domain.analytics.dto.response.AnalyticsResponse;
import bd.pro.saumik.shrnkit.domain.analytics.service.AnalyticsService;
import bd.pro.saumik.shrnkit.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/urls/{id}")
    public ResponseEntity<AnalyticsResponse> analytics(
            Authentication authentication,
            @PathVariable UUID id
    ) {

        UUID userId = SecurityUtils.currentUserId(authentication);

        return ResponseEntity.ok(
                analyticsService.getAnalytics(userId, id)
        );
    }

}