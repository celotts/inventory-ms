package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
}