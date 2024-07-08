package org.abondar.industrial.heromanager.actuator;

import lombok.RequiredArgsConstructor;
import org.abondar.industrial.heromanager.service.AuthService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServerHealthIndicator implements HealthIndicator {

    private final AuthService authService;

    @Override
    public Health health() {
        try {
            boolean isUp = Boolean.TRUE.equals(authService.isAuthServerUp().block());
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
