package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryRequestDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        var dto = new CategoryRequestDto("Salsas", "Salsas caseras", true, "admin", "admin");

        assertEquals("Salsas", dto.getName());
        assertEquals("Salsas caseras", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("admin", dto.getUpdatedBy());
    }

    @Test
    void testBuilder() {
        var dto = CategoryRequestDto.builder()
                .name("Postres")
                .description("Dulces y postres")
                .active(false)
                .createdBy("test")
                .updatedBy("test")
                .build();

        assertEquals("Postres", dto.getName());
        assertFalse(dto.getActive());
    }


    @Test
    void testNoArgsConstructorAndSetters() {
        var dto = new CategoryRequestDto();
        dto.setName("Bebidas");
        dto.setDescription("Refrescos y jugos");
        dto.setActive(true);
        dto.setCreatedBy("creator");
        dto.setUpdatedBy("updater");

        assertEquals("Bebidas", dto.getName());
        assertEquals("Refrescos y jugos", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("creator", dto.getCreatedBy());
        assertEquals("updater", dto.getUpdatedBy());
    }

    @Test
    void testToString() {
        var dto = new CategoryRequestDto("Panadería", "Productos de panadería", true, "autor", "editor");
        String toString = dto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Panadería"));
        assertTrue(toString.contains("Productos de panadería"));
        assertTrue(toString.contains("true"));
        assertTrue(toString.contains("autor"));
        assertTrue(toString.contains("editor"));
    }
}