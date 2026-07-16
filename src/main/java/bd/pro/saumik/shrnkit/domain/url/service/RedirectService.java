package bd.pro.saumik.shrnkit.domain.url.service;

import bd.pro.saumik.shrnkit.common.cache.CachedUrl;
import bd.pro.saumik.shrnkit.common.cache.UrlCacheService;
import bd.pro.saumik.shrnkit.common.exception.ShortUrlNotFoundException;
import bd.pro.saumik.shrnkit.domain.url.entity.ShortUrl;
import bd.pro.saumik.shrnkit.domain.url.mapper.ShortUrlMapper;
import bd.pro.saumik.shrnkit.domain.url.model.RedirectResult;
import bd.pro.saumik.shrnkit.domain.url.repository.ShortUrlRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final ShortUrlRepository repository;
    private final UrlCacheService cacheService;
    private final ShortUrlMapper mapper;

    @Transactional(readOnly = true)
    public RedirectResult resolve(String shortCode) {

        Optional<CachedUrl> cached = cacheService.getByShortCode(shortCode);

        if (cached.isPresent()) {

            CachedUrl url = cached.get();

            if (!url.canRedirect()) {
                throw new ShortUrlNotFoundException();
            }

            return new RedirectResult(
                    url.shortUrlId(),
                    url.originalUrl()
            );
        }

        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(ShortUrlNotFoundException::new);

        if (!shortUrl.canRedirect()) {
            throw new ShortUrlNotFoundException();
        }

        cacheService.put(mapper.toCachedUrl(shortUrl));

        return new RedirectResult(
                shortUrl.getId(),
                shortUrl.getOriginalUrl()
        );
    }

}