package bd.pro.saumik.shrnkit.domain.url.service;

import bd.pro.saumik.shrnkit.common.exception.ShortUrlNotFoundException;
import bd.pro.saumik.shrnkit.domain.url.entity.ShortUrl;
import bd.pro.saumik.shrnkit.domain.url.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlOwnershipService {
    private final ShortUrlRepository shortUrlRepository;

    public ShortUrl getOwnedUrl(UUID userId, UUID urlId) {

        ShortUrl shortUrl = shortUrlRepository.findById(urlId)
                .orElseThrow(ShortUrlNotFoundException::new);

        if (!shortUrl.getOwnerId().equals(userId)) {
            throw new AccessDeniedException("You do not own this URL.");
        }

        return shortUrl;
    }
}
