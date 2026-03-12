package com.celotts.productservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Profile("!test")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilitar @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()          // Health checks
                        .requestMatchers("/swagger-ui/**").permitAll()        // Swagger UI
                        .requestMatchers("/v3/api-docs/**").permitAll()       // OpenAPI docs
                        .anyRequest().authenticated()                         // Todo lo demás requiere autenticación
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // Configurar como Resource Server JWT

        return http.build();
    }
}
