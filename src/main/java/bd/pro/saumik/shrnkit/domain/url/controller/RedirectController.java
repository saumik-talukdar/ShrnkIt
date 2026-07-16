package bd.pro.saumik.shrnkit.domain.url.controller;

import bd.pro.saumik.shrnkit.domain.analytics.service.AnalyticsService;
import bd.pro.saumik.shrnkit.domain.url.model.RedirectResult;
import bd.pro.saumik.shrnkit.domain.url.service.RedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/c")
@RequiredArgsConstructor
public class RedirectController {

    private final RedirectService redirectService;
    private final AnalyticsService analyticsService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode
    ) {

        RedirectResult result = redirectService.resolve(shortCode);
        analyticsService.recordClick(
                result.shortUrlId()
        );
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(result.originalUrl()))
                .build();
    }

}