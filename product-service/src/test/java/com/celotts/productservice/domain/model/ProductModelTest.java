package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelTest {

    @Test
    void builder_shouldBuildProductModelCorrectly() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductModel product = ProductModel.builder()
                .id(id)
                .code("P001")
                .name("Producto Test")
                .description("Descripción del producto")
                .categoryId(categoryId)
                .unitCode("UN")
                .brandId(brandId)
                .minimumStock(10)
                .currentStock(5)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("tester")
                .updatedBy("tester")
                .build();

        assertNotNull(product);
        assertEquals("P001", product.getCode());
        assertEquals("Producto Test", product.getName());
        assertEquals(brandId, product.getBrandId());
        assertEquals(BigDecimal.valueOf(99.99), product.getUnitPrice());
    }

    @Test
    void lowStock_shouldReturnTrueWhenCurrentStockIsLessThanMinimum() {
        ProductModel product = ProductModel.builder()
                .currentStock(3)
                .minimumStock(5)
                .build();

        assertTrue(product.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalseWhenCurrentStockIsGreaterOrEqualToMinimum() {
        ProductModel product1 = ProductModel.builder()
                .currentStock(10)
                .minimumStock(5)
                .build();

        ProductModel product2 = ProductModel.builder()
                .currentStock(5)
                .minimumStock(5)
                .build();

        assertFalse(product1.lowStock());
        assertFalse(product2.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalseWhenCurrentOrMinimumStockIsNull() {
        ProductModel product1 = ProductModel.builder()
                .currentStock(null)
                .minimumStock(5)
                .build();

        ProductModel product2 = ProductModel.builder()
                .currentStock(5)
                .minimumStock(null)
                .build();

        assertFalse(product1.lowStock());
        assertFalse(product2.lowStock());
    }

    @Test
    void withMethod_shouldCreateNewInstanceWithModifiedField() {
        ProductModel original = ProductModel.builder()
                .name("Original")
                .build();

        ProductModel modified = original.withName("Modificado");

        assertEquals("Original", original.getName());
        assertEquals("Modificado", modified.getName());
        assertNotSame(original, modified);
    }

    @Test
    void equalsAndHashCode_shouldBehaveAsExpected() {
        UUID id = UUID.randomUUID();

        ProductModel p1 = ProductModel.builder().id(id).code("X1").build();
        ProductModel p2 = ProductModel.builder().id(id).code("X1").build();

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void toString_shouldContainKeyFields() {
        ProductModel product = ProductModel.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("ToStringTest")
                .build();

        String result = product.toString();
        assertTrue(result.contains("ToStringTest"));
        assertTrue(result.contains("00000000-0000-0000-0000-000000000001"));
    }
}