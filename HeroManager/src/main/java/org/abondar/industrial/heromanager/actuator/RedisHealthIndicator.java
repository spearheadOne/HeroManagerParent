package org.abondar.industrial.heromanager.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHealthIndicator implements HealthIndicator {


    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Health health() {
        try {
            var pong = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equals(pong)) {
                return Health.up().withDetail("Redis", "Available").build();
            } else {
                return Health.down().withDetail("Redis", "Unavailable").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("Redis", "Error occured").build();
        }
    }
}
