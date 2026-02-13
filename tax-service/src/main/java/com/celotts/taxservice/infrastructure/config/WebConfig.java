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
                String origin = (props.cors() != null) ? props.cors().allowedOrigin() : "*";
                if (origin == null) origin = "*";

                if ("*".equals(origin)) {
                    registry.addMapping("/**")
                            .allowedOriginPatterns("*") // Usar patterns cuando es comodín
                            .allowedMethods("*")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                } else {
                    registry.addMapping("/**")
                            .allowedOrigins(origin) // Usar origins explícitos si hay uno definido
                            .allowedMethods("*")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                }
            }
        };
    }
}
