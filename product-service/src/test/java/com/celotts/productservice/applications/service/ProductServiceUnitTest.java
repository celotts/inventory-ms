package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.usecase.ProductUseCaseImpl;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.domain.port.product.port.output.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    private ProductUseCase productService;

    @Mock
    private ProductRequestMapper productRequestMapper;

    @BeforeEach
    void setUp() {
        productService = new ProductUseCaseImpl(
                productRepositoryPort,
                productUnitPort,
                productBrandPort,
                categoryRepositoryPort,
                productRequestMapper
        );
    }

    @Test
    void createProduct_shouldCreateProductSuccessfully() {
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductCreateDto dto = ProductCreateDto.builder()
                .code("NEW001")
                .name("New Product")
                .createdBy("admin")
                .unitCode("UNIT01")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(29.99))
                .enabled(true)
                .build();

        ProductModel productModelMock = ProductModel.builder()
                .id(UUID.randomUUID())
                .code(dto.getCode())
                .name(dto.getName())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .build();

        when(productRepositoryPort.findByCode(dto.getCode())).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode("UNIT01")).thenReturn(true);
        when(productBrandPort.existsById(brandId)).thenReturn(true);
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(true);
        when(productRequestMapper.toModel(any(ProductCreateDto.class))).thenReturn(productModelMock);
        when(productRepositoryPort.save(any(ProductModel.class))).thenReturn(productModelMock);

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
        ProductCreateDto dto = ProductCreateDto.builder()
                .code("EXISTING_CODE")
                .name("New Product")
                .createdBy("admin")
                .unitCode("UNIT01")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .build();

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

        ProductUpdateDto dto = ProductUpdateDto.builder().build();
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

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("UPDATEDCODE")
                .name("Updated Name")
                .unitCode("UNIT01")
                .brandId(existingProduct.getBrandId())
                .categoryId(existingProduct.getCategoryId())
                .build();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productUnitPort.existsByCode("UNIT01")).thenReturn(true);
        when(productBrandPort.existsById(existingProduct.getBrandId())).thenReturn(true);
        when(categoryRepositoryPort.existsById(existingProduct.getCategoryId())).thenReturn(true);
        when(productRequestMapper.toModel((ProductUpdateDto) any())).thenReturn(existingProduct);
        when(productRepositoryPort.save(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = productService.updateProduct(productId, dto);

        assertNotNull(result);
        assertEquals(dto.getCode(), result.getCode());
        assertEquals(dto.getName(), result.getName());
        verify(productRepositoryPort).save(any(ProductModel.class));
    }
}