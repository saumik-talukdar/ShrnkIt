package bd.pro.saumik.shrnkit.common.cache;

import java.util.Optional;

public interface UrlCacheService {

    Optional<CachedUrl> getByShortCode(String shortCode);

    void put(CachedUrl cachedUrl);

    void evictByShortCode(String shortCode);

}