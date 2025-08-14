package com.celotts.productservice.infrastructure.common.config;

import com.celotts.productserviceOld.applications.service.ProductBrandService;
import com.celotts.productserviceOld.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MockBeansConfig {

    @Bean
    @Primary
    public ProductBrandUseCase productBrandUseCase() {
        return mock(ProductBrandUseCase.class);
    }

    @Bean
    @Primary
    public ProductBrandDtoMapper productBrandDtoMapper() {
        return mock(ProductBrandDtoMapper.class);
    }

    @Bean
    @Primary
    public ProductBrandService productBrandService() {
        return mock(ProductBrandService.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}