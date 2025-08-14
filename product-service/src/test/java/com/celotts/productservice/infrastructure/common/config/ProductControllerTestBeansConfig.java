package com.celotts.productservice.infrastructure.common.config;

import com.celotts.productserviceOld.applications.service.ProductUnitService;
import com.celotts.productserviceOld.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ProductControllerTestBeansConfig {

    @Bean
    public ProductUnitService productUnitService() {
        return mock(ProductUnitService.class);
    }

    @Bean
    public ProductUnitResponseMapper productUnitResponseMapper() {
        return mock(ProductUnitResponseMapper.class);
    }

    @Bean
    public ProductUseCase productUseCase() {
        return mock(ProductUseCase.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}