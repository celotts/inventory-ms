package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitDtoMapperTest {

    private final ProductUnitDtoMapper mapper = new ProductUnitDtoMapper();

    @Test
    void toModel_shouldMapFieldsCorrectly() {
        ProductUnitCreateDto createDto = ProductUnitCreateDto.builder()
                .code("KG")
                .name("Kilogramo")
                .description("Unidad de peso")
                .symbol("kg")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        ProductUnitModel model = mapper.toModel(createDto);

        assertNotNull(model);
        assertEquals("KG", model.getCode());
        assertEquals("Kilogramo", model.getName());
        assertEquals("Unidad de peso", model.getDescription());
        assertEquals("kg", model.getSymbol());
        assertTrue(model.getEnabled());
        assertEquals("admin", model.getCreatedBy());
        assertEquals("editor", model.getUpdatedBy());
        assertNotNull(model.getCreatedAt());
        assertNull(model.getUpdatedAt());
    }

    @Test
    void toModel_shouldReturnNull_whenDtoIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toResponse_shouldMapFieldsCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        ProductUnitModel model = ProductUnitModel.builder()
                .id(id)
                .code("L")
                .name("Litro")
                .description("Unidad de volumen")
                .symbol("L")
                .enabled(true)
                .createdBy("system")
                .updatedBy("admin")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        ProductUnitResponseDto response = mapper.toResponse(model);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("L", response.getCode());
        assertEquals("Litro", response.getName());
        assertEquals("Unidad de volumen", response.getDescription());
        assertTrue(response.isEnabled());
        assertEquals("system", response.getCreatedBy());
        assertEquals("admin", response.getUpdatedBy());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    void toResponse_shouldReturnNull_whenModelIsNull() {
        assertNull(mapper.toResponse(null));
    }
}