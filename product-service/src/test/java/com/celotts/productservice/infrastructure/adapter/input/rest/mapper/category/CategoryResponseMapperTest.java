package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryResponseMapperTest {

    @Test
    void toDto_shouldMapFieldsCorrectly() {
        UUID id = UUID.randomUUID();

        CategoryModel model = CategoryModel.builder()
                .id(id)
                .name("Electrónica")
                .description("Tecnología y gadgets")
                .active(true)
                .build();

        CategoryResponseDto dto = CategoryResponseMapper.toDto(model);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Electrónica", dto.getName());
        assertEquals("Tecnología y gadgets", dto.getDescription());
        assertTrue(dto.getActive());
    }

    @Test
    void toDto_shouldThrowException_whenModelIsNull() {
        assertThrows(NullPointerException.class, () -> {
            CategoryResponseMapper.toDto(null);
        });
    }
}