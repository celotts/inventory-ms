package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitModelTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String code = "PU123";
        String name = "Unidad";
        String description = "Descripción";
        String symbol = "kg";
        Boolean enabled = true;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "user";

        ProductUnitModel model = new ProductUnitModel(id, code, name, description, symbol,
                enabled, createdAt, updatedAt, createdBy, updatedBy);

        assertEquals(id, model.getId());
        assertEquals(code, model.getCode());
        assertEquals(name, model.getName());
        assertEquals(description, model.getDescription());
        assertEquals(symbol, model.getSymbol());
        assertEquals(enabled, model.getEnabled());
        assertEquals(createdAt, model.getCreatedAt());
        assertEquals(updatedAt, model.getUpdatedAt());
        assertEquals(createdBy, model.getCreatedBy());
        assertEquals(updatedBy, model.getUpdatedBy());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ProductUnitModel model = new ProductUnitModel();

        UUID id = UUID.randomUUID();
        model.setId(id);
        model.setCode("PU456");
        model.setName("Otro");
        model.setDescription("Desc");
        model.setSymbol("L");
        model.setEnabled(false);
        model.setCreatedAt(LocalDateTime.now().minusDays(2));
        model.setUpdatedAt(LocalDateTime.now());
        model.setCreatedBy("usuario");
        model.setUpdatedBy("otro");

        assertEquals(id, model.getId());
        assertEquals("PU456", model.getCode());
        assertEquals("Otro", model.getName());
        assertEquals("Desc", model.getDescription());
        assertEquals("L", model.getSymbol());
        assertFalse(model.getEnabled());
        assertNotNull(model.getCreatedAt());
        assertNotNull(model.getUpdatedAt());
        assertEquals("usuario", model.getCreatedBy());
        assertEquals("otro", model.getUpdatedBy());
    }

    @Test
    void testBuilder() {
        ProductUnitModel model = ProductUnitModel.builder()
                .code("BUILDER_CODE")
                .name("BuilderName")
                .symbol("g")
                .build();

        assertEquals("BUILDER_CODE", model.getCode());
        assertEquals("BuilderName", model.getName());
        assertEquals("g", model.getSymbol());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        ProductUnitModel m1 = ProductUnitModel.builder().id(id).code("EQ").build();
        ProductUnitModel m2 = ProductUnitModel.builder().id(id).code("EQ").build();

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testNotEqualsDifferentFields() {
        ProductUnitModel m1 = ProductUnitModel.builder().code("ONE").build();
        ProductUnitModel m2 = ProductUnitModel.builder().code("TWO").build();

        assertNotEquals(m1, m2);
    }

    @Test
    void testToString() {
        ProductUnitModel model = ProductUnitModel.builder()
                .code("STR")
                .name("ToString")
                .symbol("kg")
                .build();

        String str = model.toString();
        assertNotNull(str);
        assertTrue(str.contains("STR"));
        assertTrue(str.contains("ToString"));
    }

    @Test
    void testWithMethods() {
        ProductUnitModel original = ProductUnitModel.builder().code("original").build();
        ProductUnitModel modified = original.withCode("modified");

        assertEquals("original", original.getCode());
        assertEquals("modified", modified.getCode());
        assertNotEquals(original, modified);
    }

    @Test
    void testAllWithMethods() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductUnitModel base = ProductUnitModel.builder()
                .id(id)
                .code("CODE")
                .name("Name")
                .description("Desc")
                .symbol("S")
                .enabled(true)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        ProductUnitModel modified = base
                .withId(UUID.randomUUID())
                .withCode("NEW_CODE")
                .withName("NewName")
                .withDescription("NewDesc")
                .withSymbol("kg")
                .withEnabled(false)
                .withCreatedAt(now.minusDays(2))
                .withUpdatedAt(now.plusDays(1))
                .withCreatedBy("nuevo")
                .withUpdatedBy("otro");

        assertNotEquals(base, modified);
    }

    @Test
    void testCanEqualWithDifferentObject() {
        ProductUnitModel model = ProductUnitModel.builder().code("A").build();
        assertNotEquals(model, "some string"); // Esto fuerza la ejecución de canEqual()
    }
}