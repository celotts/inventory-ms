package com.celotts.productservice.infrastructure.common.config;

import com.celotts.productserviceOld.applications.service.ProductUnitService;
import com.celotts.productserviceOld.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productserviceOld.domain.port.product.unit.usecase.ProductUnitUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.spy;

@TestConfiguration
public class TestBeanConfig {

    @Bean
    @Primary // le damos prioridad a este bean si hay más de uno del mismo tipo
    public ProductUnitUseCase productUnitUseCase() {
        return Mockito.mock(ProductUnitUseCase.class);
    }

    @Bean
    @Primary
    public ProductUnitRepositoryPort productUnitRepositoryPort() {
        return Mockito.mock(ProductUnitRepositoryPort.class);
    }

    @Bean
    public ProductUnitService productUnitService(
            ProductUnitUseCase useCase,
            ProductUnitRepositoryPort repo,
            ProductUnitDtoMapper mapper
    ) {
        return spy(new ProductUnitService(useCase, repo, mapper));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}