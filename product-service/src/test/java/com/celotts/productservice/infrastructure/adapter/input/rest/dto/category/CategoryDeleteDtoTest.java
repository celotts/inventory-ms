package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CategoryDeleteDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        var dto = new CategoryDeleteDto(id);

        assertEquals(id, dto.getId());
    }

    @Test
    void testSetters() {
        UUID id = UUID.randomUUID();
        var dto = new CategoryDeleteDto();
        dto.setId(id);

        assertEquals(id, dto.getId());
    }


    @Test
    void testNoArgsConstructorAndToString() {
        CategoryDeleteDto dto = new CategoryDeleteDto();
        assertNotNull(dto.toString()); // Verifica que toString no sea null
    }
}