package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitDeleteDtoTest {

    @Test
    void lombokDataWorks() {
        UUID id = UUID.randomUUID();
        ProductUnitDeleteDto a = new ProductUnitDeleteDto();
        a.setId(id);

        ProductUnitDeleteDto b = new ProductUnitDeleteDto(id);

        assertEquals(id, a.getId());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("ProductUnitDeleteDto"));
    }
}