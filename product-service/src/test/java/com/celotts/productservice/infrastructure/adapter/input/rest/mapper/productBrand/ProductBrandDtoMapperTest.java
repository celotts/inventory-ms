package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandDtoMapperTest {

    private ProductBrandDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductBrandDtoMapper();
    }

    @Test
    void toResponseDto_shouldMapFieldsCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandModel model = ProductBrandModel.builder()
                .id(id)
                .name("Marca Test")
                .description("Descripción de prueba")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester2")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductBrandResponseDto dto = mapper.toResponseDto(model);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Marca Test", dto.getName());
        assertEquals("Descripción de prueba", dto.getDescription());
        assertTrue(dto.getEnabled());
        assertEquals("tester", dto.getCreatedBy());
        assertEquals("tester2", dto.getUpdatedBy());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void toResponseDto_shouldReturnNull_whenModelIsNull() {
        ProductBrandResponseDto dto = mapper.toResponseDto(null);
        assertNull(dto);
    }

    @Test
    void toModel_shouldMapFieldsCorrectly() {
        ProductBrandCreateDto createDto = ProductBrandCreateDto.builder()
                .name("Marca A")
                .description("Descripción A")
                .enabled(true)
                .createdBy("user1")
                .updatedBy("user2")
                .build();

        ProductBrandModel model = mapper.toModel(createDto);

        assertNotNull(model);
        assertEquals("Marca A", model.getName());
        assertEquals("Descripción A", model.getDescription());
        assertTrue(model.getEnabled());
        assertEquals("user1", model.getCreatedBy());
        assertEquals("user2", model.getUpdatedBy());
        assertNotNull(model.getCreatedAt());
        assertNull(model.getUpdatedAt()); // ← según implementación
    }

    @Test
    void toModel_shouldReturnNull_whenDtoIsNull() {
        ProductBrandModel model = mapper.toModel(null);
        assertNull(model);
    }

    @Test
    void toCreateDto_shouldMapFieldsCorrectly() {
        ProductBrandModel model = ProductBrandModel.builder()
                .name("Marca A")
                .description("Desc A")
                .build();

        ProductBrandCreateDto dto = mapper.toCreateDto(model);

        assertNotNull(dto);
        assertEquals("Marca A", dto.getName());
        assertEquals("Desc A", dto.getDescription());
    }

    @Test
    void toCreateDto_shouldReturnNull_whenModelIsNull() {
        ProductBrandCreateDto dto = mapper.toCreateDto(null);
        assertNull(dto);
    }

    @Test
    void toResponseDtoList_shouldMapListCorrectly() {
        ProductBrandModel model1 = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Brand 1")
                .description("Desc 1")
                .enabled(true)
                .createdBy("u1")
                .updatedBy("u2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductBrandModel model2 = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Brand 2")
                .description("Desc 2")
                .enabled(false)
                .createdBy("u3")
                .updatedBy("u4")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<ProductBrandResponseDto> dtos = mapper.toResponseDtoList(List.of(model1, model2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        assertEquals("Brand 1", dtos.get(0).getName());
        assertEquals("Brand 2", dtos.get(1).getName());
    }

    @Test
    void toResponseDtoList_shouldReturnNull_whenInputIsNull() {
        List<ProductBrandResponseDto> dtos = mapper.toResponseDtoList(null);
        assertNull(dtos);
    }
}