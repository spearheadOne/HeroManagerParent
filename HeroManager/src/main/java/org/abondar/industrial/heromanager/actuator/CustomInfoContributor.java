package org.abondar.industrial.heromanager.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomInfoContributor  implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        var customInfo = Map.of(
                "app_name", "My Spring Boot Application",
                "app_version", "1.0.0",
                "description", "Hero Manager API.",
                "additional_info", "Part of experimental development program"
        );

        builder.withDetail("customInfo",customInfo);
    }
}
