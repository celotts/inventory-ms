package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestDtoTest {

    @Test
    void testGettersAndSetters() {
        ProductRequestDto dto = new ProductRequestDto();

        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        dto.setCode("CODE123");
        dto.setName("Test Product");
        dto.setDescription("Sample description");
        dto.setCategoryId(categoryId);
        dto.setUnitCode("KG");
        dto.setBrandId(brandId);
        dto.setMinimumStock(5);
        dto.setCurrentStock(20);
        dto.setUnitPrice(new BigDecimal("15.75"));
        dto.setEnabled(false);
        dto.setCreatedBy("tester");
        dto.setUpdatedBy("tester2");

        assertEquals("CODE123", dto.getCode());
        assertEquals("Test Product", dto.getName());
        assertEquals("Sample description", dto.getDescription());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals("KG", dto.getUnitCode());
        assertEquals(brandId, dto.getBrandId());
        assertEquals(5, dto.getMinimumStock());
        assertEquals(20, dto.getCurrentStock());
        assertEquals(new BigDecimal("15.75"), dto.getUnitPrice());
        assertFalse(dto.getEnabled());
        assertEquals("tester", dto.getCreatedBy());
        assertEquals("tester2", dto.getUpdatedBy());
    }

    @Test
    void testAllArgsConstructorAndEqualsHashCodeToString() {
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductRequestDto dto1 = new ProductRequestDto(
                "CODE123",
                "Product",
                "Description",
                categoryId,
                "UNIT",
                brandId,
                1,
                10,
                new BigDecimal("9.99"),
                true,
                "creator",
                "updater"
        );

        ProductRequestDto dto2 = new ProductRequestDto(
                "CODE123",
                "Product",
                "Description",
                categoryId,
                "UNIT",
                brandId,
                1,
                10,
                new BigDecimal("9.99"),
                true,
                "creator",
                "updater"
        );

        assertEquals(dto1, dto2); // equals()
        assertEquals(dto1.hashCode(), dto2.hashCode()); // hashCode()
        assertNotNull(dto1.toString()); // toString() coverage
    }

    @Test
    void testBuilder() {
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductRequestDto dto = ProductRequestDto.builder()
                .code("PRD001")
                .name("Builder Product")
                .description("Built via builder")
                .categoryId(categoryId)
                .unitCode("L")
                .brandId(brandId)
                .minimumStock(2)
                .currentStock(50)
                .unitPrice(new BigDecimal("99.99"))
                .enabled(true)
                .createdBy("builder")
                .updatedBy("updater")
                .build();

        assertEquals("PRD001", dto.getCode());
        assertEquals("Builder Product", dto.getName());
        assertEquals("Built via builder", dto.getDescription());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals("L", dto.getUnitCode());
        assertEquals(brandId, dto.getBrandId());
        assertEquals(2, dto.getMinimumStock());
        assertEquals(50, dto.getCurrentStock());
        assertEquals(new BigDecimal("99.99"), dto.getUnitPrice());
        assertTrue(dto.getEnabled());
        assertEquals("builder", dto.getCreatedBy());
        assertEquals("updater", dto.getUpdatedBy());
    }

    @Test
    void testNotEquals() {
        ProductRequestDto dto1 = ProductRequestDto.builder().code("ABC").build();
        ProductRequestDto dto2 = ProductRequestDto.builder().code("DEF").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testDefaultEnabledValue() {
        ProductRequestDto dto = new ProductRequestDto();
        assertTrue(dto.getEnabled()); // debe ser true por defecto por @Builder.Default
    }

    @Test
    void testEquals_withNullAndDifferentClass() {
        ProductRequestDto dto = new ProductRequestDto();
        assertNotEquals(dto, null); // Comparación con null
        assertNotEquals(dto, "A string"); // Comparación con otra clase
    }

    @Test
    void testEquals_sameReference() {
        ProductRequestDto dto = new ProductRequestDto();
        assertEquals(dto, dto); // Comparación con sí mismo
    }

    @Test
    void testEquals_differentObjectsWithDifferentFields() {
        ProductRequestDto dto1 = ProductRequestDto.builder().code("X").build();
        ProductRequestDto dto2 = ProductRequestDto.builder().code("Y").build();
        assertNotEquals(dto1, dto2); // Diferente contenido
    }

    @Test
    void testHashCode_differentObjectsProduceDifferentHash() {
        ProductRequestDto dto1 = ProductRequestDto.builder().code("X").build();
        ProductRequestDto dto2 = ProductRequestDto.builder().code("Y").build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testHashCode_sameFieldsProduceSameHash() {
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductRequestDto dto1 = ProductRequestDto.builder()
                .code("A")
                .name("Name")
                .description("Desc")
                .categoryId(categoryId)
                .unitCode("U")
                .brandId(brandId)
                .minimumStock(1)
                .currentStock(5)
                .unitPrice(new BigDecimal("10.00"))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        ProductRequestDto dto2 = ProductRequestDto.builder()
                .code("A")
                .name("Name")
                .description("Desc")
                .categoryId(categoryId)
                .unitCode("U")
                .brandId(brandId)
                .minimumStock(1)
                .currentStock(5)
                .unitPrice(new BigDecimal("10.00"))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}