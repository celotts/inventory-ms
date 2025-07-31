package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBrandServiceTest {

    @Mock
    private ProductBrandUseCase productBrandUseCase;

    @Mock
    private ProductBrandDtoMapper dtoMapper;

    @InjectMocks
    private ProductBrandService productBrandService;

    private ProductBrandCreateDto createDto;
    private ProductBrandModel model;
    private ProductBrandResponseDto responseDto;

    private UUID id;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        now = LocalDateTime.now();

        createDto = ProductBrandCreateDto.builder()
                .name("BrandX")
                .description("Descripción de prueba")
                .enabled(true)
                .createdBy("test-user")
                .updatedBy("test-user")
                .build();

        model = ProductBrandModel.builder()
                .id(id)
                .name("BrandX")
                .description("Descripción de prueba")
                .enabled(true)
                .createdBy("test-user")
                .updatedBy("test-user")
                .createdAt(now)
                .updatedAt(now)
                .build();

        responseDto = ProductBrandResponseDto.builder()
                .id(id)
                .name("BrandX")
                .description("Descripción de prueba")
                .enabled(true)
                .createdBy("test-user")
                .updatedBy("test-user")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void create_shouldReturnResponseDto_whenBrandIsCreatedSuccessfully() {
        // Arrange
        when(productBrandUseCase.existsByName(eq("BrandX"))).thenReturn(false);
        when(dtoMapper.toModel(any(ProductBrandCreateDto.class))).thenReturn(model);
        when(productBrandUseCase.save(any(ProductBrandModel.class))).thenReturn(model);
        when(dtoMapper.toResponseDto(model)).thenReturn(responseDto);

        // Act
        ProductBrandResponseDto result = productBrandService.create(createDto);

        // Assert
        assertNotNull(result);
        assertEquals("BrandX", result.getName());
        assertEquals("Descripción de prueba", result.getDescription());

        verify(productBrandUseCase).existsByName(eq("BrandX"));
        verify(dtoMapper).toModel(any(ProductBrandCreateDto.class));
        verify(productBrandUseCase).save(any(ProductBrandModel.class));
        verify(dtoMapper).toResponseDto(any(ProductBrandModel.class));
    }

    @Test
    void create_shouldThrowException_whenBrandAlreadyExists() {
        when(productBrandUseCase.existsByName("BrandX")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productBrandService.create(createDto));

        System.out.println("Mensaje real: " + exception.getMessage());
        assertEquals("ProductBrand with name 'BrandX' already exists", exception.getMessage());
        verify(productBrandUseCase).existsByName("BrandX");
        verifyNoMoreInteractions(dtoMapper, productBrandUseCase);
    }
}