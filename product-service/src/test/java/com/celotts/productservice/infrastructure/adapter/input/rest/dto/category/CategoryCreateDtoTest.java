package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryCreateDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        var dto = new CategoryCreateDto("Bebidas", "Categoría para bebidas frías");

        assertEquals("Bebidas", dto.getName());
        assertEquals("Categoría para bebidas frías", dto.getDescription());
    }

    @Test
    void testSetters() {
        var dto = new CategoryCreateDto();
        dto.setName("Comidas");
        dto.setDescription("Platillos preparados");

        assertEquals("Comidas", dto.getName());
        assertEquals("Platillos preparados", dto.getDescription());
    }
}