package org.abondar.industrial.heromanager.actuator;

import lombok.RequiredArgsConstructor;
import org.abondar.industrial.heromanager.service.AuthService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="auth.enabled", havingValue = "true")
@RequiredArgsConstructor
public class AuthServerHealthIndicator implements HealthIndicator {

    private final AuthService authService;

    @Override
    public Health health() {
        try {
            var isUp = Boolean.TRUE.equals(authService.isAuthServerUp().block());
            if (isUp) {
                return Health.up().build();
            } else {
                return Health.down().build();
            }
        } catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}
