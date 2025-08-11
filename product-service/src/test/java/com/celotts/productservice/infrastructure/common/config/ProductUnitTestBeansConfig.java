package com.celotts.productservice.infrastructure.common.config;

import com.celotts.productservice.applications.service.ProductUnitService;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.domain.port.product.unit.usecase.ProductUnitUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@TestConfiguration
public class ProductUnitTestBeansConfig {

    @Bean
    @Primary
    public ProductUnitUseCase productUnitUseCase() {
        return mock(ProductUnitUseCase.class);
    }

    @Bean
    @Primary
    public ProductUnitRepositoryPort productUnitRepositoryPort() {
        return mock(ProductUnitRepositoryPort.class);
    }

    @Bean
    public ProductUnitDtoMapper productUnitDtoMapper() {
        return mock(ProductUnitDtoMapper.class);
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