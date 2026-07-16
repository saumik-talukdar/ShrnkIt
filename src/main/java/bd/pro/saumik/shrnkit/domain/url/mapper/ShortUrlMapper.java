package bd.pro.saumik.shrnkit.domain.url.mapper;

import bd.pro.saumik.shrnkit.common.cache.CachedUrl;
import bd.pro.saumik.shrnkit.domain.url.dto.response.ShortUrlResponse;
import bd.pro.saumik.shrnkit.domain.url.dto.response.UrlSummaryResponse;
import bd.pro.saumik.shrnkit.domain.url.entity.ShortUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlMapper {

    @Value("${app.base-url}")
    private String baseUrl;

    public ShortUrlResponse toResponse(ShortUrl url) {

        return new ShortUrlResponse(

                url.getId(),

                url.getOriginalUrl(),

                url.getShortCode(),

                baseUrl + "/" + url.getShortCode(),

                url.getExpiresAt(),

                url.getCreatedAt()
        );
    }

    public UrlSummaryResponse toSummary(ShortUrl url) {

        return new UrlSummaryResponse(

                url.getId(),

                url.getShortCode(),

                baseUrl + "/" + url.getShortCode(),

                url.getOriginalUrl(),

                url.isActive(),

                url.getExpiresAt(),

                url.getCreatedAt()
        );
    }

    public CachedUrl toCachedUrl(ShortUrl shortUrl) {

        return new CachedUrl(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrl.getOriginalUrl(),
                shortUrl.isActive(),
                shortUrl.getExpiresAt()
        );
    }


}