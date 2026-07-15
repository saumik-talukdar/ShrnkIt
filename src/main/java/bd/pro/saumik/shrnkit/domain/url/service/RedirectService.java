package bd.pro.saumik.shrnkit.domain.url.service;

import bd.pro.saumik.shrnkit.common.cache.UrlCacheService;
import bd.pro.saumik.shrnkit.common.exception.ShortUrlNotFoundException;
import bd.pro.saumik.shrnkit.domain.url.entity.ShortUrl;
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

    @Transactional(readOnly = true)
    public String resolve(String shortCode) {

        Optional<String> cached = cacheService.get(shortCode);

        if (cached.isPresent()) {
            return cached.get();
        }

        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(ShortUrlNotFoundException::new);

        if (!shortUrl.canRedirect()) {
            throw new ShortUrlNotFoundException();
        }

        cacheService.put(
                shortCode,
                shortUrl.getOriginalUrl()
        );

        return shortUrl.getOriginalUrl();
    }

}