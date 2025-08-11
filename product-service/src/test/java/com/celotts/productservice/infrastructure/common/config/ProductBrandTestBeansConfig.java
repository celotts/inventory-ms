package com.celotts.productservice.infrastructure.common.config;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ProductBrandTestBeansConfig {

    @Bean
    public ProductBrandUseCase productBrandUseCase() {
        return mock(ProductBrandUseCase.class);
    }

    @Bean
    public ProductBrandService productBrandService() {
        return mock(ProductBrandService.class);
    }

    @Bean
    public ProductBrandDtoMapper productBrandDtoMapper() {
        return mock(ProductBrandDtoMapper.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}