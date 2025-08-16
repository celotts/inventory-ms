package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductUnitAuditable;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitAuditableTest {

    @Test
    void builder_shouldBuildCorrectInstance() {
        LocalDateTime now = LocalDateTime.now();

        ProductUnitAuditable unit = ProductUnitAuditable.builder()
                .code("UNIT001")
                .name("Kilogram")
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        assertNotNull(unit);
        assertEquals("UNIT001", unit.getCode());
        assertEquals("Kilogram", unit.getName());
        assertEquals(now, unit.getCreatedAt());
        assertEquals(now, unit.getUpdatedAt());
        assertEquals("admin", unit.getCreatedBy());
        assertEquals("admin", unit.getUpdatedBy());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        ProductUnitAuditable unit = new ProductUnitAuditable();
        assertNotNull(unit);
        assertNull(unit.getCode());
        assertNull(unit.getName());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        ProductUnitAuditable unit = new ProductUnitAuditable();
        LocalDateTime now = LocalDateTime.now();

        unit.setCode("UNIT002");
        unit.setName("Piece");
        unit.setCreatedAt(now);
        unit.setUpdatedAt(now);
        unit.setCreatedBy("user");
        unit.setUpdatedBy("user");

        assertEquals("UNIT002", unit.getCode());
        assertEquals("Piece", unit.getName());
        assertEquals(now, unit.getCreatedAt());
        assertEquals(now, unit.getUpdatedAt());
        assertEquals("user", unit.getCreatedBy());
        assertEquals("user", unit.getUpdatedBy());
    }

    @Test
    void equalsAndHashCode_shouldBehaveCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        ProductUnitAuditable unit1 = ProductUnitAuditable.builder()
                .code("UNIT003")
                .name("Liters")
                .createdAt(now)
                .updatedAt(now)
                .createdBy("user1")
                .updatedBy("user1")
                .build();

        ProductUnitAuditable unit2 = ProductUnitAuditable.builder()
                .code("UNIT003")
                .name("Liters")
                .createdAt(now)
                .updatedAt(now)
                .createdBy("user1")
                .updatedBy("user1")
                .build();

        assertEquals(unit1, unit2);
        assertEquals(unit1.hashCode(), unit2.hashCode());
    }

    @Test
    void toString_shouldContainClassName() {
        ProductUnitAuditable unit = new ProductUnitAuditable();
        String str = unit.toString();
        assertTrue(str.contains("ProductUnitAuditable"));
    }
}