package org.abondar.industrial.heromanager.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abondar.industrial.heromanager.authentication.JwtAuthenticationToken;
import org.abondar.industrial.heromanager.service.AuthService;
import org.abondar.industrial.heromanager.service.TokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@ConditionalOnProperty(name="auth.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {


    private static final String BEARER_PREFIX = "Bearer ";

    private static final String AUTH_HEADER = "Authorization";

    private final AuthService authService;

    private final TokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            var token = authHeader.substring(BEARER_PREFIX.length());
            if (tokenService.tokenExists(token)){
              authenticate(token);
            } else {
                var isAuthenticated = Boolean.TRUE.equals(authService.authenticate(token).block());
                if (isAuthenticated) {
                    tokenService.saveToken(token);
                    authenticate(token);
                }
            }
        } else {
            log.error("Missing authorization header");
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token){
        Authentication authentication = new JwtAuthenticationToken(token);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
