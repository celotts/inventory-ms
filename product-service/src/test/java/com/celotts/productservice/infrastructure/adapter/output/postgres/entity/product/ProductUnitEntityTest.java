package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitEntityTest {

    @Test
    void testSettersAndToString() {
        ProductUnitEntity unit = new ProductUnitEntity();
        UUID id = UUID.randomUUID();
        String code = "U001";
        String name = "Unidad";
        String description = "Unidad de peso";
        String symbol = "kg";
        Boolean enabled = true;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "editor";

        unit.setId(id);
        unit.setCode(code);
        unit.setName(name);
        unit.setDescription(description);
        unit.setSymbol(symbol);
        unit.setEnabled(enabled);
        unit.setCreatedAt(createdAt);
        unit.setUpdatedAt(updatedAt);
        unit.setCreatedBy(createdBy);
        unit.setUpdatedBy(updatedBy);

        assertEquals(id, unit.getId());
        assertEquals(code, unit.getCode());
        assertEquals(name, unit.getName());
        assertEquals(description, unit.getDescription());
        assertEquals(symbol, unit.getSymbol());
        assertEquals(enabled, unit.getEnabled());
        assertEquals(createdAt, unit.getCreatedAt());
        assertEquals(updatedAt, unit.getUpdatedAt());
        assertEquals(createdBy, unit.getCreatedBy());
        assertEquals(updatedBy, unit.getUpdatedBy());

        String toString = unit.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("U001"));
        assertTrue(toString.contains("Unidad"));
        assertTrue(toString.contains("kg"));
    }

    @Test
    void testEqualsHashCodeCanEqual() {
        UUID id = UUID.randomUUID();

        ProductUnitEntity u1 = ProductUnitEntity.builder()
                .id(id)
                .code("U100")
                .name("Unidad A")
                .build();

        ProductUnitEntity u2 = ProductUnitEntity.builder()
                .id(id)
                .code("U100")
                .name("Unidad A")
                .build();

        ProductUnitEntity u3 = ProductUnitEntity.builder()
                .id(UUID.randomUUID())
                .code("U200")
                .name("Unidad B")
                .build();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1, u3);
        assertNotEquals(u1.hashCode(), u3.hashCode());

        assertTrue(u1.canEqual(u2));
        assertFalse(u1.canEqual("String"));
    }

    @Test
    void testToBuilder() {
        ProductUnitEntity original = ProductUnitEntity.builder()
                .code("U999")
                .name("Original")
                .enabled(true)
                .build();

        ProductUnitEntity copy = original.toBuilder()
                .name("Copiado")
                .enabled(false)
                .build();

        assertEquals("U999", copy.getCode());
        assertEquals("Copiado", copy.getName());
        assertFalse(copy.getEnabled());
        assertNotEquals(original, copy);
    }
}