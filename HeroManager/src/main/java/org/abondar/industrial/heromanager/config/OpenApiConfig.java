package org.abondar.industrial.heromanager.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name="auth.enabled", havingValue = "false")
public class OpenApiConfig {

    @Bean
    public OpenAPI jwtAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hero API")
                        .description("Hero management API")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License()
                                .name("MIT")));

    }
}
