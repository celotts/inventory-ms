package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.port.output.product.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private ProductUseCaseImpl productUseCase;

    private ProductModel product;

    @BeforeEach
    void setUp() {
        product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("TEST-001")
                .name("Test Product")
                .build();
    }

    @Test
    @DisplayName("Should throw exception when creating a product with an existing code")
    void createProduct_whenCodeExists_shouldThrowException() {
        // Given
        when(productRepositoryPort.findByCode("TEST-001")).thenReturn(Optional.of(product));

        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            productUseCase.createProduct(product);
        });

        verify(productRepositoryPort, never()).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("Should create product successfully when code is new")
    void createProduct_whenCodeIsNew_shouldSaveProduct() {
        // Given
        when(productRepositoryPort.findByCode("TEST-001")).thenReturn(Optional.empty());
        when(productRepositoryPort.save(any(ProductModel.class))).thenReturn(product);

        // When
        ProductModel result = productUseCase.createProduct(product);

        // Then
        assertNotNull(result);
        assertEquals("TEST-001", result.getCode());
        verify(productRepositoryPort, times(1)).save(product);
    }
}
