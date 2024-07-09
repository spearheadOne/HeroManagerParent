package org.abondar.industrial.heromanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name="auth.enabled", havingValue = "true")
public class OpenApiSecureConfig {

    @Bean
    public OpenAPI jwtAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hero API")
                        .description("Hero management API")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License()
                                .name("MIT")))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)));

    }
}
