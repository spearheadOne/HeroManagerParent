package org.abondar.industrial.heromanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abondar.industrial.heromanager.properties.AuthServerProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthServerProperties authServerProperties;

    private final WebClient.Builder webClient;

    public Mono<Boolean> authenticate(String token) {
        return webClient
                .baseUrl(authServerProperties.getHost())
                .build()
                .post()
                .uri(authServerProperties.getAuthUrl())
                .headers(
                        httpHeaders -> httpHeaders.setBearerAuth(token)
                )
               .exchangeToMono(clientResponse -> {
                   if (clientResponse.statusCode().is2xxSuccessful()){
                       return Mono.just(true);
                   } else {
                       return Mono.just(false);
                   }
               })
               .doOnError(error -> log.error("Error authenticating by token: {}", error.getMessage()))
               .onErrorReturn(false);

    }

    public Mono<Boolean> isAuthServerUp() {
        return webClient
                .baseUrl(authServerProperties.getHost())
                .build()
                .get()
                .uri(authServerProperties.getHealthUrl())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()){
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                })
                .doOnError(error -> log.error("Error checking authentication server health: {}", error.getMessage()))
                .onErrorReturn(false);
    }
}
