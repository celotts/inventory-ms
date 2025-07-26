package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUpdateDtoTest {

    @Test
    void shouldAccessAllFieldsAndGetters() {
        UUID id = UUID.randomUUID();
        String code = "P-001";
        String name = "Producto A";
        String description = "Descripción del producto";
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        String unitCode = "KG";
        int minimumStock = 5;
        int currentStock = 10;
        BigDecimal unitPrice = new BigDecimal("99.99");
        String createdBy = "admin";
        String updatedBy = "editor";
        boolean enabled = true;

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .categoryId(categoryId)
                .brandId(brandId)
                .unitCode(unitCode)
                .minimumStock(minimumStock)
                .currentStock(currentStock)
                .unitPrice(unitPrice)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .enabled(enabled)
                .build();

        // 👇 Invocar explícitamente los getters que faltan
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(id, dto.getId());

        // También validamos otros por buenas prácticas
        assertEquals(unitCode, dto.getUnitCode());
        assertEquals(code, dto.getCode());
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals(brandId, dto.getBrandId());
        assertEquals(minimumStock, dto.getMinimumStock());
        assertEquals(currentStock, dto.getCurrentStock());
        assertEquals(unitPrice, dto.getUnitPrice());
        assertEquals(updatedBy, dto.getUpdatedBy());
        assertEquals(enabled, dto.getEnabled());
    }
}