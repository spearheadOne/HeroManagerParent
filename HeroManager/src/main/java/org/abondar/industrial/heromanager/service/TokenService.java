package org.abondar.industrial.heromanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private static final Integer TOKEN_EXPIRATION = 600;
    private static final String REDIS_KEY = "authToken:";

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveToken(String token) {
        redisTemplate.opsForValue().set(REDIS_KEY + token, token, TOKEN_EXPIRATION, TimeUnit.SECONDS);
        log.info("Saving token new token");
    }

    public boolean tokenExists(String token) {
        var res = redisTemplate.opsForSet().getOperations().hasKey(REDIS_KEY + token);
        log.info("Token exists: {}",res);
        return Boolean.TRUE.equals(res);
    }
}
