package org.abondar.industrial.heromanager.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class DatabasebHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;
    private static final String VALIDATION_QUERY = "SELECT 1";

    @Override
    public Health health() {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(VALIDATION_QUERY);
            return Health.up().build();
        } catch (SQLException e) {
            return Health.down(e).build();
        }
    }

}
