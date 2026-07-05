package bd.pro.saumik.shrnkit.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final StringRedisTemplate redisTemplate;
    private final String PREFIX = "EMAIL_VERIFICATION_";
    private final long ttl = 60;

    public String createToken(UUID userId){
        String token = UUID.randomUUID().toString();
        String key = PREFIX + token;
        redisTemplate.opsForValue().set(key,userId.toString(), Duration.ofMinutes(ttl));
        return token;
    }

    public UUID validateToken(String token){
        String key = PREFIX + token;
        String value = redisTemplate.opsForValue().get(key);
        if(Objects.isNull(value)){
            return null;
        }
        return UUID.fromString(value);
    }

    public void deleteToken(String token){
        String key = PREFIX + token;
        redisTemplate.delete(key);
    }
}