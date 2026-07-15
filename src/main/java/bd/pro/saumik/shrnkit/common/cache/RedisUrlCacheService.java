package bd.pro.saumik.shrnkit.common.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisUrlCacheService implements UrlCacheService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "url:";
    @Value("${cache.url.ttl}")
    private long ttl;

    @Override
    public Optional<String> get(String shortCode) {
        return Optional.ofNullable(
                stringRedisTemplate.opsForValue().get(PREFIX + shortCode)
        );
    }

    @Override
    public void put(String shortCode, String originalUrl) {
        stringRedisTemplate.opsForValue().set(
                PREFIX + shortCode,
                originalUrl,
                Duration.ofSeconds(ttl)
        );
    }

    @Override
    public void evict(String shortCode) {
        stringRedisTemplate.delete(PREFIX + shortCode);
    }
}
