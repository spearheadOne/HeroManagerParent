package org.abondar.industrial.heromanager.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth")
@Getter
@Setter
public class AuthProperties {

    private boolean enabled;
}
