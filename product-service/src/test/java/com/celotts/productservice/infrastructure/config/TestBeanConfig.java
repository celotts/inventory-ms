package com.celotts.productservice.infrastructure.config;

import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.domain.port.product.unit.usecase.ProductUnitUseCase;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestBeanConfig {


    @Bean
    @Primary
    public ProductUnitUseCase mockProductUnitUseCase() {
        return Mockito.mock(ProductUnitUseCase.class);
    }

    @Bean
    @Primary
    public ProductBrandUseCase mockProductBrandUseCase() {
        return Mockito.mock(ProductBrandUseCase.class);
    }
}