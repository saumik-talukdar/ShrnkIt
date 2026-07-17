package bd.pro.saumik.shrnkit.domain.url.controller;

import bd.pro.saumik.shrnkit.common.http.VisitorService;
import bd.pro.saumik.shrnkit.domain.analytics.service.AnalyticsService;
import bd.pro.saumik.shrnkit.domain.url.model.RedirectResult;
import bd.pro.saumik.shrnkit.domain.url.service.RedirectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/c")
@RequiredArgsConstructor
public class RedirectController {

    private final RedirectService redirectService;
    private final AnalyticsService analyticsService;
    private final VisitorService visitorService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        UUID visitorId = visitorService.resolveVisitor(request, response);

        RedirectResult result = redirectService.resolve(shortCode);
        analyticsService.recordClick(
                result.shortUrlId(),
                visitorId
        );
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(result.originalUrl()))
                .build();
    }

}