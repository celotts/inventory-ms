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

    @Test
    void equals_nullVsNonNull_branch_onAnotherField() {
        // null vs non-null en un campo distinto para cubrir otra rama ternaria
        ProductUnitModel a = ProductUnitModel.builder()
                .name(null).build();
        ProductUnitModel b = ProductUnitModel.builder()
                .name("Name").build();
        assertNotEquals(a, b);                   // rama: left null, right non-null
    }

    @Test
    void equals_nonNullAndDifferentValue_branch() {
        // Ambos no-nulos pero distintos en al menos un campo → false
        ProductUnitModel a = ProductUnitModel.builder().code("A").build();
        ProductUnitModel b = ProductUnitModel.builder().code("B").build();
        assertNotEquals(a, b);                   // rama: nonNull && !equals
    }

    private ProductUnitModel full(UUID id) {
        LocalDateTime now = LocalDateTime.now();
        return ProductUnitModel.builder()
                .id(id)
                .code("C")
                .name("N")
                .description("D")
                .symbol("kg")
                .enabled(Boolean.TRUE)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .createdBy("creator")
                .updatedBy("updater")
                .build();
    }

    @Test
    void equals_allNullFields_branch_and_hashCode_nullPath() {
        // Ambos con todos (o casi todos) los campos nulos → camino null==null
        ProductUnitModel a = ProductUnitModel.builder()
                .id(null).code(null).name(null).description(null)
                .symbol(null).enabled(null).createdAt(null).updatedAt(null)
                .createdBy(null).updatedBy(null)
                .build();

        ProductUnitModel b = ProductUnitModel.builder()
                .id(null).code(null).name(null).description(null)
                .symbol(null).enabled(null).createdAt(null).updatedAt(null)
                .createdBy(null).updatedBy(null)
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode()); // rama de hash con nulos
    }

    // Subclase que fuerza canEqual=false
    static class FakeSubclass extends ProductUnitModel {
        public FakeSubclass() { super(); }
        @Override public boolean canEqual(Object other) { return false; }
    }

    @Test
    void equals_shouldBeFalse_whenCanEqualReturnsFalse() {
        ProductUnitModel base = ProductUnitModel.builder().code("X").build();
        FakeSubclass sub = new FakeSubclass();

        // Ambas direcciones ejercitan la llamada a canEqual(...)
        assertFalse(base.equals(sub));  // base.canEqual(sub) -> true, pero sub.canEqual(base) -> false
        assertFalse(sub.equals(base));  // aquí directamente canEqual false
    }
}