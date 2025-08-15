package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeEntityTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        String code = "TYPE001";
        String name = "Perecedero";
        String description = "Productos con fecha de vencimiento";
        Boolean enabled = true;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "mod";

        ProductTypeEntity entity = ProductTypeEntity.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
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
        assertEquals(enabled, entity.getEnabled());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(createdBy, entity.getCreatedBy());
        assertEquals(updatedBy, entity.getUpdatedBy());
    }

    @Test
    void testSettersAndNoArgsConstructor() {
        ProductTypeEntity entity = new ProductTypeEntity();

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setCode("TYPE002");
        entity.setName("No Perecedero");
        entity.setDescription("Productos de larga duración");
        entity.setEnabled(false);
        entity.setCreatedAt(now.minusDays(5));
        entity.setUpdatedAt(now);
        entity.setCreatedBy("creator");
        entity.setUpdatedBy("editor");

        assertEquals(id, entity.getId());
        assertEquals("TYPE002", entity.getCode());
        assertEquals("No Perecedero", entity.getName());
        assertEquals("Productos de larga duración", entity.getDescription());
        assertFalse(entity.getEnabled());
        assertEquals(now.minusDays(5), entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
        assertEquals("creator", entity.getCreatedBy());
        assertEquals("editor", entity.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();

        ProductTypeEntity e1 = ProductTypeEntity.builder()
                .id(id)
                .code("T01")
                .name("Perecedero")
                .build();

        ProductTypeEntity e2 = ProductTypeEntity.builder()
                .id(id)
                .code("T01")
                .name("Perecedero")
                .build();

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentValues() {
        ProductTypeEntity e1 = ProductTypeEntity.builder()
                .id(UUID.randomUUID())
                .code("T01")
                .name("Tipo A")
                .build();

        ProductTypeEntity e2 = ProductTypeEntity.builder()
                .id(UUID.randomUUID())
                .code("T02")
                .name("Tipo B")
                .build();

        assertNotEquals(e1, e2);
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testCanEqual() {
        ProductTypeEntity e1 = new ProductTypeEntity();
        ProductTypeEntity e2 = new ProductTypeEntity();

        assertTrue(e1.canEqual(e2));
        assertFalse(e1.canEqual("not-an-entity"));
    }

    @Test
    void testToString() {
        ProductTypeEntity entity = ProductTypeEntity.builder()
                .code("TS01")
                .name("Tipo String")
                .createdBy("admin")
                .build();

        String result = entity.toString();

        assertNotNull(result);
        assertTrue(result.contains("TS01"));
        assertTrue(result.contains("Tipo String"));
        assertTrue(result.contains("admin"));
    }

    @Test
    void testEqualsWithNullAndSameInstance() {
        ProductTypeEntity entity = ProductTypeEntity.builder()
                .id(UUID.randomUUID())
                .code("EQ01")
                .name("Comparar")
                .build();

        assertEquals(entity, entity); // misma instancia
        assertNotEquals(entity, null); // null
        assertNotEquals(entity, "otro objeto"); // otro tipo
    }
}