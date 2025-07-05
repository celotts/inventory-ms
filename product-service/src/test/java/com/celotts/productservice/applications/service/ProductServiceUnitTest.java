package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.domain.port.product.root.output.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private ProductUnitRepositoryPort productUnitPort;

    @Mock
    private ProductBrandPort productBrandPort;

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(
                productRepositoryPort,
                productUnitPort,
                productBrandPort,
                categoryRepositoryPort
        );
    }

    @Test
    void createProduct_shouldCreateProductSuccessfully() {
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setCode("NEW001");
        dto.setName("New Product");
        dto.setCreatedBy("admin");
        dto.setUnitCode("UNIT01");
        dto.setBrandId(brandId);
        dto.setCategoryId(categoryId);
        dto.setMinimumStock(10);
        dto.setCurrentStock(50);
        dto.setUnitPrice(BigDecimal.valueOf(29.99));
        dto.setEnabled(true);

        when(productRepositoryPort.findByCode(dto.getCode())).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode("UNIT01")).thenReturn(true);
        when(productBrandPort.existsById(brandId)).thenReturn(true);
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(true);
        when(productRepositoryPort.save(any(ProductModel.class))).thenReturn(
                ProductModel.builder()
                        .id(UUID.randomUUID())
                        .code(dto.getCode())
                        .name(dto.getName())
                        .unitCode(dto.getUnitCode())
                        .brandId(dto.getBrandId())
                        .categoryId(dto.getCategoryId())
                        .build()
        );

        ProductModel result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals(dto.getCode(), result.getCode());
        verify(productRepositoryPort).findByCode(dto.getCode());
        verify(productUnitPort).existsByCode("UNIT01");
        verify(productBrandPort).existsById(brandId);
        verify(categoryRepositoryPort).existsById(categoryId);
        verify(productRepositoryPort).save(any(ProductModel.class));
    }

    @Test
    void createProduct_shouldThrowProductAlreadyExistsExceptionWhenCodeAlreadyExists() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setCode("EXISTING_CODE");
        dto.setName("New Product");
        dto.setCreatedBy("admin");
        dto.setUnitCode("UNIT01");
        dto.setBrandId(UUID.randomUUID());
        dto.setCategoryId(UUID.randomUUID());

        ProductModel existingProduct = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("EXISTING_CODE")
                .name("Existing Product")
                .build();

        when(productRepositoryPort.findByCode(dto.getCode())).thenReturn(Optional.of(existingProduct));

        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class, () -> {
            productService.createProduct(dto);
        });

        assertTrue(exception.getMessage().contains("EXISTING_CODE"));
        verify(productRepositoryPort).findByCode(dto.getCode());
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void updateProduct_shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        when(productRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setCode("NEWCODE999");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(nonExistentId, dto);
        });

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
        verify(productRepositoryPort).findById(nonExistentId);
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void updateProduct_shouldUpdateProductSuccessfully() {
        UUID productId = UUID.randomUUID();

        ProductModel existingProduct = ProductModel.builder()
                .id(productId)
                .code("CODE123")
                .name("Original Name")
                .unitCode("UNIT01")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .createdBy("tester")
                .build();

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setCode("UPDATEDCODE");
        dto.setName("Updated Name");
        dto.setUnitCode("UNIT01");
        dto.setBrandId(existingProduct.getBrandId());
        dto.setCategoryId(existingProduct.getCategoryId());

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productUnitPort.existsByCode("UNIT01")).thenReturn(true);
        when(productBrandPort.existsById(existingProduct.getBrandId())).thenReturn(true);
        when(categoryRepositoryPort.existsById(existingProduct.getCategoryId())).thenReturn(true);
        when(productRepositoryPort.save(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = productService.updateProduct(productId, dto);

        assertNotNull(result);
        assertEquals(dto.getCode(), result.getCode());
        assertEquals(dto.getName(), result.getName());
        verify(productRepositoryPort).save(any(ProductModel.class));
    }
}