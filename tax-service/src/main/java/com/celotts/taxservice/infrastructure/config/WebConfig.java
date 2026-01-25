package com.celotts.taxservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebConfig {

    @Bean
    WebMvcConfigurer corsConfigurer(AppProperties props) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // Verificamos si props.cors() es nulo antes de llamar a allowedOrigin()
                String origin = (props.cors() != null) ? props.cors().allowedOrigin() : "*";

                // Si por alguna razón origin sigue siendo nulo, usamos "*"
                if (origin == null) {
                    origin = "*";
                }

                registry.addMapping("/**")
                        .allowedOrigins(origin)
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}