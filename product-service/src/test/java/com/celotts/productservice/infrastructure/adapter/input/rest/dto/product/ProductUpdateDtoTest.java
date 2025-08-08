package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUpdateDtoTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        ProductUpdateDto dto = new ProductUpdateDto();
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        dto.setId(id);
        dto.setCode("PRD-001");
        dto.setName("Product");
        dto.setDescription("Description");
        dto.setCategoryId(categoryId);
        dto.setUnitCode("KG");
        dto.setBrandId(brandId);
        dto.setMinimumStock(10);
        dto.setCurrentStock(100);
        dto.setUnitPrice(new BigDecimal("15.99"));
        dto.setEnabled(true);
        dto.setCreatedBy("admin");
        dto.setUpdatedBy("admin");

        assertEquals(id, dto.getId());
        assertEquals("PRD-001", dto.getCode());
        assertEquals("Product", dto.getName());
        assertEquals("Description", dto.getDescription());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals("KG", dto.getUnitCode());
        assertEquals(brandId, dto.getBrandId());
        assertEquals(10, dto.getMinimumStock());
        assertEquals(100, dto.getCurrentStock());
        assertEquals(new BigDecimal("15.99"), dto.getUnitPrice());
        assertTrue(dto.getEnabled());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("admin", dto.getUpdatedBy());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductUpdateDto dto = new ProductUpdateDto(
                id, "P01", "Nombre", "desc", categoryId, "KG",
                brandId, 5, 10, new BigDecimal("9.99"),
                "creator", true, "updator"
        );

        assertEquals("P01", dto.getCode());
        assertEquals("Nombre", dto.getName());
        assertEquals("desc", dto.getDescription());
        assertEquals("KG", dto.getUnitCode());
        assertEquals(new BigDecimal("9.99"), dto.getUnitPrice());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .id(id)
                .code("ABC123")
                .name("Test Product")
                .description("Sample description")
                .categoryId(categoryId)
                .unitCode("UNIT")
                .brandId(brandId)
                .minimumStock(5)
                .currentStock(20)
                .unitPrice(new BigDecimal("10.00"))
                .enabled(false)
                .createdBy("system")
                .updatedBy("user")
                .build();

        assertEquals("ABC123", dto.getCode());
        assertEquals("Test Product", dto.getName());
        assertEquals(5, dto.getMinimumStock());
        assertEquals(20, dto.getCurrentStock());
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        UUID id = UUID.randomUUID();
        ProductUpdateDto dto1 = ProductUpdateDto.builder().id(id).code("X").build();
        ProductUpdateDto dto2 = ProductUpdateDto.builder().id(id).code("X").build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        ProductUpdateDto dto1 = ProductUpdateDto.builder().code("A").build();
        ProductUpdateDto dto2 = ProductUpdateDto.builder().code("B").build();

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        ProductUpdateDto dto = new ProductUpdateDto();
        assertNotEquals(dto, null);
        assertNotEquals(dto, "not a dto");
    }

    @Test
    void testEqualsSameInstance() {
        ProductUpdateDto dto = new ProductUpdateDto();
        assertEquals(dto, dto);
    }

    @Test
    void testToStringIsNotNull() {
        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("ABC123")
                .name("Product")
                .build();

        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("code=ABC123"));
    }

    @Test
    void testGetCreatedByAndUnitCode() {
        ProductUpdateDto dto = ProductUpdateDto.builder()
                .unitCode("LITERS")
                .createdBy("admin")
                .build();

        assertEquals("LITERS", dto.getUnitCode());
        assertEquals("admin", dto.getCreatedBy());
    }

    @Test
    void testBuilderToBuilder() {
        ProductUpdateDto original = ProductUpdateDto.builder()
                .code("X1")
                .name("Old")
                .build();

        ProductUpdateDto modified = original.toBuilder()
                .name("New")
                .build();

        assertEquals("X1", modified.getCode());
        assertEquals("New", modified.getName());
    }
}