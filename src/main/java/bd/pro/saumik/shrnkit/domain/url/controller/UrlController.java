package bd.pro.saumik.shrnkit.domain.url.controller;

import bd.pro.saumik.shrnkit.domain.url.dto.request.CreateShortUrlRequest;
import bd.pro.saumik.shrnkit.domain.url.dto.request.UpdateShortUrlRequest;
import bd.pro.saumik.shrnkit.domain.url.dto.response.ShortUrlResponse;
import bd.pro.saumik.shrnkit.domain.url.dto.response.UrlSummaryResponse;
import bd.pro.saumik.shrnkit.domain.url.service.UrlService;
import bd.pro.saumik.shrnkit.domain.user.entity.User;
import bd.pro.saumik.shrnkit.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<ShortUrlResponse> create(
            Authentication authentication,
            @Valid @RequestBody CreateShortUrlRequest request
    ) {

        UUID userId = SecurityUtils.currentUserId(authentication);
        if(userId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                urlService.create(userId, request)
        );
    }

    @GetMapping
    public ResponseEntity<Page<UrlSummaryResponse>> getMyUrls(
            Authentication authentication,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        UUID userId = SecurityUtils.currentUserId(authentication);

        return ResponseEntity.ok(
                urlService.getUserUrls(
                        userId,
                        pageable
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        if(userId == null) {
            return ResponseEntity.badRequest().build();
        }
        urlService.delete(userId, id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UrlSummaryResponse> update(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateShortUrlRequest request
    ) {

        UUID userId = SecurityUtils.currentUserId(authentication);
        if(userId == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                urlService.update(userId, id, request)
        );
    }
}