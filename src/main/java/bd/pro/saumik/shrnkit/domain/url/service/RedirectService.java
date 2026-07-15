package bd.pro.saumik.shrnkit.domain.url.service;

import bd.pro.saumik.shrnkit.common.exception.ShortUrlNotFoundException;
import bd.pro.saumik.shrnkit.domain.url.entity.ShortUrl;
import bd.pro.saumik.shrnkit.domain.url.repository.ShortUrlRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final ShortUrlRepository repository;

    @Transactional(readOnly = true)
    public String resolve(String shortCode) {

        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(ShortUrlNotFoundException::new);

        if (!shortUrl.canRedirect()) {
            throw new ShortUrlNotFoundException();
        }

        return shortUrl.getOriginalUrl();
    }

}