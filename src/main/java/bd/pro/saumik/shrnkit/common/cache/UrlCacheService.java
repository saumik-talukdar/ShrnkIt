package bd.pro.saumik.shrnkit.common.cache;

import java.util.Optional;

public interface UrlCacheService {

    Optional<String> get(String shortCode);

    void put(String shortCode, String originalUrl);

    void evict(String shortCode);

}