package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductBrandServiceTest {

    @Mock
    private ProductBrandUseCase productBrandUseCase;

    @Mock
    private ProductBrandDtoMapper dtoMapper;

    @InjectMocks
    private ProductBrandService productBrandService;

    private UUID brandId;
    private ProductBrandModel brandModel;
    private ProductBrandResponseDto responseDto;

    @BeforeEach
    void setUp() {
        brandId = UUID.randomUUID();
        brandModel = ProductBrandModel.builder()
                .id(brandId)
                .name("Nike")
                .description("Sports brand")
                .enabled(true)
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .build();

        responseDto = ProductBrandResponseDto.builder()
                .id(brandModel.getId())
                .name(brandModel.getName())
                .description(brandModel.getDescription())
                .enabled(brandModel.getEnabled())
                .createdBy(brandModel.getCreatedBy())
                .createdAt(brandModel.getCreatedAt())
                .build();
    }

    @Test
    void create_shouldThrowIfNameExists() {
        ProductBrandCreateDto dto = ProductBrandCreateDto.builder()
                .name("Nike")
                .description("Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        when(productBrandUseCase.existsByName("Nike")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productBrandService.create(dto));
    }



    @Test
    void findById_shouldThrowIfNotFound() {
        when(productBrandUseCase.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productBrandService.findById(brandId));
    }



    @Test
    void findByName_shouldThrowIfNotFound() {
        when(productBrandUseCase.findByName("Nike")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productBrandService.findByName("Nike"));
    }

    @Test
    void delete_shouldCallDeleteById() {
        when(productBrandUseCase.existsById(brandId)).thenReturn(true);

        productBrandService.delete(brandId);

        verify(productBrandUseCase).deleteById(brandId);
    }

    @Test
    void delete_shouldThrowIfNotFound() {
        when(productBrandUseCase.existsById(brandId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productBrandService.delete(brandId));
    }



    @Test
    void existsById_shouldReturnTrue() {
        when(productBrandUseCase.existsById(brandId)).thenReturn(true);

        assertTrue(productBrandService.existsById(brandId));
    }

    //
    @Test
    void create_shouldSaveAndReturnResponseDto() {
        ProductBrandCreateDto createDto = ProductBrandCreateDto.builder()
                .name("Nike")
                .description("Description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ProductBrandModel model = ProductBrandModel.builder()
                .name("Nike")
                .description("Description")
                .enabled(true)
                .createdBy("admin")
                .build();

        ProductBrandModel savedModel = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Nike")
                .description("Description")
                .enabled(true)
                .createdBy("admin")
                .createdAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        when(productBrandUseCase.existsByName("Nike")).thenReturn(false);
        when(dtoMapper.toModel(any(ProductBrandCreateDto.class))).thenReturn(model);
        when(productBrandUseCase.save(any(ProductBrandModel.class))).thenReturn(savedModel);

        ProductBrandResponseDto result = productBrandService.create(createDto);

        assertNotNull(result);
        assertEquals("Nike", result.getName());
        assertEquals("Description", result.getDescription());

        ArgumentCaptor<ProductBrandModel> captor = ArgumentCaptor.forClass(ProductBrandModel.class);
        verify(productBrandUseCase).save(captor.capture());

        ProductBrandModel captured = captor.getValue();
        assertEquals("Nike", captured.getName());
        assertEquals("Description", captured.getDescription());

        // No verificamos dtoMapper si no está completamente mockeado

//clearAllCaches();
// Captura el argumento también del mapper


    }

    @Test
    void update_shouldUpdateAndReturnResponseDto() {
        ProductBrandUpdateDto updateDto = ProductBrandUpdateDto.builder()
                .name("NikeUpdated")
                .description("Updated Description")
                .enabled(true)
                .updatedBy("admin")
                .build();

        ProductBrandModel updatedModel = ProductBrandModel.builder()
                .id(brandId)
                .name("NikeUpdated")
                .description("Updated Description")
                .enabled(true)
                .updatedBy("admin")
                .updatedAt(LocalDateTime.now())
                .build();

        ProductBrandResponseDto updatedResponseDto = ProductBrandResponseDto.builder()
                .id(updatedModel.getId())
                .name(updatedModel.getName())
                .description(updatedModel.getDescription())
                .enabled(updatedModel.getEnabled())
                .updatedBy(updatedModel.getUpdatedBy())
                .updatedAt(updatedModel.getUpdatedAt())
                .build();

        when(productBrandUseCase.findById(brandId)).thenReturn(Optional.of(brandModel));
        when(productBrandUseCase.findByName("NikeUpdated")).thenReturn(Optional.empty());
        assertNotNull(updatedModel.getId(), "updatedModel.getId() is null");

        assertNotNull(updatedModel.getId(), "updatedModel.getId() is null");
        assertNotNull(updatedModel.getName(), "updatedModel.getName() is null");

        when(productBrandUseCase.save(any(ProductBrandModel.class))).thenReturn(updatedModel);

        ProductBrandResponseDto response = productBrandService.update(brandId, updateDto);

        assertNotNull(response);
        assertEquals("NikeUpdated", response.getName());
        verify(productBrandUseCase).save(any(ProductBrandModel.class));
    }

    @Test
    void existsByName_shouldReturnTrue() {
        when(productBrandUseCase.existsByName("Nike")).thenReturn(true);

        boolean exists = productBrandService.existsByName("Nike");

        assertTrue(exists);
    }

    @Test
    void findNameById_shouldReturnOptionalName() {
        when(productBrandUseCase.findNameById(brandId)).thenReturn(Optional.of("Nike"));

        Optional<String> name = productBrandService.findNameById(brandId);

        assertTrue(name.isPresent());
        assertEquals("Nike", name.get());
    }

    @Test
    void findAllIds_shouldReturnListOfIds() {
        List<UUID> ids = List.of(brandId);
        when(productBrandUseCase.findAllIds()).thenReturn(ids);

        List<UUID> result = productBrandService.findAllIds();

        assertEquals(1, result.size());
        assertEquals(brandId, result.get(0));
    }





}