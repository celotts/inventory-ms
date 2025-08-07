package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitEntityTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        String code = "PU001";
        String name = "Unidad";
        String description = "Descripción unidad";
        String symbol = "kg";
        Boolean enabled = true;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "editor";

        ProductUnitEntity entity = ProductUnitEntity.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .symbol(symbol)
                .enabled(enabled)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(code, entity.getCode());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertEquals(symbol, entity.getSymbol());
        assertEquals(enabled, entity.getEnabled());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(createdBy, entity.getCreatedBy());
        assertEquals(updatedBy, entity.getUpdatedBy());
    }

    @Test
    void testSettersAndNoArgsConstructor() {
        ProductUnitEntity entity = new ProductUnitEntity();

        UUID id = UUID.randomUUID();
        String code = "PU002";
        String name = "Unidad 2";
        String description = "Otra descripción";
        String symbol = "L";
        Boolean enabled = false;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);
        String createdBy = "user1";
        String updatedBy = "user2";

        entity.setId(id);
        entity.setCode(code);
        entity.setName(name);
        entity.setDescription(description);
        entity.setSymbol(symbol);
        entity.setEnabled(enabled);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setCreatedBy(createdBy);
        entity.setUpdatedBy(updatedBy);

        assertEquals(id, entity.getId());
        assertEquals(code, entity.getCode());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertEquals(symbol, entity.getSymbol());
        assertEquals(enabled, entity.getEnabled());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(createdBy, entity.getCreatedBy());
        assertEquals(updatedBy, entity.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();

        ProductUnitEntity entity1 = ProductUnitEntity.builder()
                .id(id)
                .code("CODE123")
                .build();

        ProductUnitEntity entity2 = ProductUnitEntity.builder()
                .id(id)
                .code("CODE123")
                .build();

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testToString() {
        ProductUnitEntity entity = ProductUnitEntity.builder()
                .id(UUID.randomUUID())
                .code("TOSTRING")
                .symbol("kg")
                .build();

        String toString = entity.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("TOSTRING"));
        assertTrue(toString.contains("kg"));
    }

    @Test
    void testCanEqual() {
        ProductUnitEntity entity1 = new ProductUnitEntity();
        ProductUnitEntity entity2 = new ProductUnitEntity();

        assertTrue(entity1.canEqual(entity2));
        assertFalse(entity1.canEqual("NotAProductUnitEntity"));
    }

    @Test
    void testNotEqualsDifferentFields() {
        UUID id = UUID.randomUUID();

        ProductUnitEntity unit1 = ProductUnitEntity.builder()
                .id(id)
                .code("CODE123")
                .name("Kilogramo")
                .description("Peso")
                .symbol("kg")
                .enabled(true)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ProductUnitEntity unit2 = ProductUnitEntity.builder()
                .id(id)
                .code("CODE123")
                .name("Gramo") // diferente campo
                .description("Peso")
                .symbol("g")
                .enabled(true)
                .createdAt(unit1.getCreatedAt())
                .updatedAt(unit1.getUpdatedAt())
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        assertNotEquals(unit1, unit2);
        assertNotEquals(unit1.hashCode(), unit2.hashCode());
    }

    @Test
    void testEqualsWithNullAndSameInstance() {
        ProductUnitEntity entity = ProductUnitEntity.builder()
                .id(UUID.randomUUID())
                .code("EQ001")
                .build();

        // Mismo objeto
        assertEquals(entity, entity); // this == o

        // Comparar con null
        assertNotEquals(entity, null); // o == null

        // Comparar con otro tipo de objeto
        assertNotEquals(entity, "not an entity");
    }
}