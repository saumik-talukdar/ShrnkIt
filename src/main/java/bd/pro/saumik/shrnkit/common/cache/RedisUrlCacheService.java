package bd.pro.saumik.shrnkit.common.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisUrlCacheService implements UrlCacheService {

    private final RedisTemplate<String,Object> redisTemplate;

    @Value("${cache.url.ttl}")
    private long ttl;

    @Override
    public Optional<CachedUrl> getByShortCode(String shortCode) {
        return Optional.ofNullable(
                (CachedUrl) redisTemplate.opsForValue().get(CacheKeys.url(shortCode))
        );
    }

    @Override
    public void put(CachedUrl cachedUrl) {
        redisTemplate.opsForValue().set(
                CacheKeys.url(cachedUrl.shortCode()),
                cachedUrl,
                Duration.ofSeconds(ttl)
        );
    }

    @Override
    public void evictByShortCode(String shortCode) {
        redisTemplate.delete(CacheKeys.url(shortCode));
    }
}
