package org.abondar.industrial.heromanager.config;


import org.abondar.industrial.heromanager.filter.AuthFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name="auth.enabled", havingValue = "true")
public class AuthenticationConfig {

    @Bean
    @ConditionalOnProperty(name="auth.enabled", havingValue = "true")
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

//        var allowedOrigins = List.of("http://*/v1/*","http://*/heromanager/*","htts://*/v1/*","https://*/heromanager/*");
//        var corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
//        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
//        corsConfiguration.setAllowedMethods(List.of("GET","PUT","POST","DELETE","PATCH"));
//        corsConfiguration.setAllowedHeaders(List.of("accept","Authorization","Content-type"));
//        corsConfiguration.setAllowedOriginPatterns(allowedOrigins);

        //TODO: fix failed to load remote config on enabled security
        //TODO: fix login form on disabled security

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(hs -> hs.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth-> auth.requestMatchers("/swagger/**","/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/actuator/health")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
           //     .cors(co-> co.configurationSource(cs->corsConfiguration))
                .build();

    }
}
