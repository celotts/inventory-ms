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
import java.util.*;

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

//    @Test
//    void create_shouldSaveAndReturnResponseDto() {
//        ProductBrandCreateDto createDto = ProductBrandCreateDto.builder()
//                .name("Nike")
//                .description("Description")
//                .enabled(true)
//                .createdBy("admin")
//                .updatedBy("admin")
//                .build();
//
//        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 12, 0);
//        ProductBrandModel model = ProductBrandModel.builder()
//                .name("Nike")
//                .description("Description")
//                .enabled(true)
//                .createdBy("admin")
//                .createdAt(createdAt)
//                .build();
//
//        ProductBrandModel savedModel = ProductBrandModel.builder()
//                .id(UUID.randomUUID())
//                .name("Nike")
//                .description("Description")
//                .enabled(true)
//                .createdBy("admin")
//                .createdAt(createdAt)
//                .build();
//
//        ProductBrandResponseDto expectedResponse = ProductBrandResponseDto.builder()
//                .id(savedModel.getId())
//                .name(savedModel.getName())
//                .description(savedModel.getDescription())
//                .enabled(savedModel.getEnabled())
//                .createdBy(savedModel.getCreatedBy())
//                .createdAt(createdAt)
//                .build();
//
//        when(productBrandUseCase.existsByName("Nike")).thenReturn(false);
//        when(dtoMapper.toModel(createDto)).thenReturn(model);
//        when(productBrandUseCase.save(model)).thenReturn(savedModel);
//        when(dtoMapper.toResponseDto(savedModel)).thenReturn(expectedResponse);
//
//        ProductBrandResponseDto result = productBrandService.create(createDto);
//
//        assertNotNull(result);
//        assertEquals("Nike", result.getName());
//        assertEquals("Description", result.getDescription());
//
//        verify(productBrandUseCase).save(model);
//        verify(dtoMapper).toResponseDto(savedModel);
//    }

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

//    @Test
//    void findAll_shouldReturnListOfDtos() {
//        when(productBrandUseCase.findAll()).thenReturn(Collections.singletonList(brandModel));
//        when(dtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);
//
//        List<ProductBrandResponseDto> result = productBrandService.findAll();
//
//        assertEquals(1, result.size());
//        assertEquals("Nike", result.get(0).getName());
//    }

//    @Test
//    void findById_shouldReturnDto() {
//        when(productBrandUseCase.findById(brandId)).thenReturn(Optional.of(brandModel));
//        when(dtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);
//
//        ProductBrandResponseDto result = productBrandService.findById(brandId);
//
//        assertEquals("Nike", result.getName());
//    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(productBrandUseCase.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productBrandService.findById(brandId));
    }

//    @Test
//    void findByName_shouldReturnDto() {
//        when(productBrandUseCase.findByName("Nike")).thenReturn(Optional.of(brandModel));
//        when(dtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);
//
//        ProductBrandResponseDto result = productBrandService.findByName("Nike");
//
//        assertEquals("Nike", result.getName());
//    }

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

//    @Test
//    void enableBrand_shouldReturnUpdatedDto() {
//        when(productBrandUseCase.enableBrand(brandId)).thenReturn(brandModel);
//        when(dtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);
//
//        ProductBrandResponseDto result = productBrandService.enableBrand(brandId);
//
//        assertEquals("Nike", result.getName());
//    }

//    @Test
//    void disableBrand_shouldReturnUpdatedDto() {
//        when(productBrandUseCase.disableBrand(brandId)).thenReturn(brandModel);
//        when(dtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);
//
//        ProductBrandResponseDto result = productBrandService.disableBrand(brandId);
//
//        assertEquals("Nike", result.getName());
//    }

    @Test
    void existsById_shouldReturnTrue() {
        when(productBrandUseCase.existsById(brandId)).thenReturn(true);

        assertTrue(productBrandService.existsById(brandId));
    }
}