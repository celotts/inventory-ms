package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryUpdateDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        var dto = new CategoryUpdateDto("Bebidas", "Refrescos", true, "admin");

        assertEquals("Bebidas", dto.getName());
        assertEquals("Refrescos", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("admin", dto.getUpdatedBy());
    }

    @Test
    void testBuilder() {
        var dto = CategoryUpdateDto.builder()
                .name("Salsas")
                .description("Salsas picantes")
                .active(false)
                .updatedBy("tester")
                .build();

        assertEquals("Salsas", dto.getName());
        assertEquals("Salsas picantes", dto.getDescription());
        assertFalse(dto.getActive());
        assertEquals("tester", dto.getUpdatedBy());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        var dto = new CategoryUpdateDto();
        dto.setName("Postres");
        dto.setDescription("Dulces y postres");
        dto.setActive(true);
        dto.setUpdatedBy("editor");

        assertEquals("Postres", dto.getName());
        assertEquals("Dulces y postres", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("editor", dto.getUpdatedBy());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        var dto1 = CategoryUpdateDto.builder()
                .name("Sopas")
                .description("Sopas calientes")
                .active(true)
                .updatedBy("chef")
                .build();

        var dto2 = CategoryUpdateDto.builder()
                .name("Sopas")
                .description("Sopas calientes")
                .active(true)
                .updatedBy("chef")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertTrue(dto1.toString().contains("Sopas"));
    }
}