package com.celotts.productservice.infrastructure.config;

import com.celotts.productservice.applications.service.ProductUnitService;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitResponseMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockConfig {

    @Bean
    public ProductUnitService productUnitService() {
        return Mockito.mock(ProductUnitService.class);
    }

    @Bean
    public ProductUnitResponseMapper productUnitResponseMapper() {
        return Mockito.mock(ProductUnitResponseMapper.class);
    }

    @Bean
    public ProductUseCase productUseCase() {
        return Mockito.mock(ProductUseCase.class);
    }
}