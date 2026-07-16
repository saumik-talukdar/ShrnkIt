package bd.pro.saumik.shrnkit.domain.url.service;

import bd.pro.saumik.shrnkit.common.cache.UrlCacheService;
import bd.pro.saumik.shrnkit.common.exception.ShortUrlNotFoundException;
import bd.pro.saumik.shrnkit.domain.url.dto.request.CreateShortUrlRequest;
import bd.pro.saumik.shrnkit.domain.url.dto.request.UpdateShortUrlRequest;
import bd.pro.saumik.shrnkit.domain.url.dto.response.ShortUrlResponse;
import bd.pro.saumik.shrnkit.domain.url.dto.response.UrlSummaryResponse;
import bd.pro.saumik.shrnkit.domain.url.entity.ShortUrl;
import bd.pro.saumik.shrnkit.domain.url.mapper.ShortUrlMapper;
import bd.pro.saumik.shrnkit.domain.url.repository.ShortUrlRepository;
import bd.pro.saumik.shrnkit.domain.url.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository repository;

    private final ShortCodeGenerator generator;

    private final ShortUrlMapper mapper;

    private final UrlCacheService cacheService;

    private final UrlOwnershipService urlOwnershipService;

    @Transactional
    public ShortUrlResponse create(
            UUID userId,
            CreateShortUrlRequest request
    ) {

        String code;

        do {
            code = generator.generate();
        }
        while (repository.existsByShortCode(code));

        ShortUrl shortUrl = ShortUrl.builder()
                .ownerId(userId)
                .originalUrl(request.originalUrl())
                .shortCode(code)
                .expiresAt(request.expiresAt())
                .build();

        repository.save(shortUrl);

        return mapper.toResponse(shortUrl);
    }

    @Transactional(readOnly = true)
    public Page<UrlSummaryResponse> getUserUrls(
            UUID ownerId,
            Pageable pageable
    ) {

        return repository.findByOwnerId(ownerId, pageable)
                .map(mapper::toSummary);
    }

    @Transactional
    public void delete(
            UUID userId,
            UUID urlId
    ) {

        ShortUrl shortUrl = urlOwnershipService.getOwnedUrl(userId, urlId);

        shortUrl.deactivate();

        cacheService.evictByShortCode(shortUrl.getShortCode());
    }

    @Transactional
    public UrlSummaryResponse update(
            UUID userId,
            UUID urlId,
            UpdateShortUrlRequest request
    ) {

        ShortUrl shortUrl = urlOwnershipService.getOwnedUrl(userId, urlId);

        if (request.originalUrl() != null) {
            shortUrl.updateOriginalUrl(request.originalUrl());
        }

        if (request.expiresAt() != null) {
            shortUrl.updateExpiration(request.expiresAt());
        }

        if (request.active() != null) {
            if (request.active()) {
                shortUrl.activate();
            } else {
                shortUrl.deactivate();
            }
        }

        cacheService.evictByShortCode(shortUrl.getShortCode());

        return mapper.toSummary(shortUrl);
    }

}