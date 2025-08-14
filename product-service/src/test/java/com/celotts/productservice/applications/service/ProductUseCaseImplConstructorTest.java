package com.celotts.productservice.applications.service;

import com.celotts.productserviceOld.applications.usecase.ProductUseCaseImpl;
import com.celotts.productserviceOld.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productserviceOld.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productserviceOld.domain.port.product.port.output.ProductRepositoryPort;
import com.celotts.productserviceOld.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ProductUseCaseImplConstructorTest {

    @Test
    void shouldInstantiateProductUseCaseImplConstructor() {
        // Arrange
        ProductRepositoryPort productRepositoryPort = mock(ProductRepositoryPort.class);
        ProductUnitRepositoryPort productUnitPort = mock(ProductUnitRepositoryPort.class);
        ProductBrandPort productBrandPort = mock(ProductBrandPort.class);
        CategoryRepositoryPort categoryRepositoryPort = mock(CategoryRepositoryPort.class);
        ProductRequestMapper productRequestMapper = mock(ProductRequestMapper.class);

        // Act
        ProductUseCaseImpl useCase = new ProductUseCaseImpl(
                productRepositoryPort,
                productUnitPort,
                productBrandPort,
                categoryRepositoryPort,
                productRequestMapper
        );

        // Assert
        assertNotNull(useCase);
    }

}