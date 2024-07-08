package org.abondar.industrial.heromanager.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-server")
@Getter
@Setter
public class AuthServerProperties {

    private String host;

    private String authUrl;

    private String healthUrl;
}
