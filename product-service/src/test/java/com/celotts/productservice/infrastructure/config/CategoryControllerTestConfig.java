package com.celotts.productservice.infrastructure.config;

import com.celotts.productservice.domain.port.category.usecase.CategoryUseCase;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@TestConfiguration
public class CategoryControllerTestConfig {

    @Bean
    public CategoryUseCase categoryUseCase() {
        return Mockito.mock(CategoryUseCase.class);
    }

    // Útil si el test inyecta ObjectMapper
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}