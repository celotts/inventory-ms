package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitModelEqualsHashCodeExtraTest {

    private ProductUnitModel full(UUID id) {
        LocalDateTime now = LocalDateTime.now();
        return ProductUnitModel.builder()
                .id(id)
                .code("CODE")
                .name("Name")
                .description("Desc")
                .symbol("kg")
                .enabled(true)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .createdBy("creator")
                .updatedBy("updater")
                .build();
    }

    @Test
    void equals_shouldBeTrue_forSameReference() {
        ProductUnitModel a = full(UUID.randomUUID());
        assertTrue(a.equals(a));      // rama: misma instancia
    }

    @Test
    void equals_shouldBeFalse_againstNull() {
        ProductUnitModel a = full(UUID.randomUUID());
        assertFalse(a.equals(null));  // rama: null
    }

    @Test
    void equals_shouldBeTrue_whenAllFieldsEqualEvenIfNulls() {
        // objetos con varios campos en null para ejercitar ramas de comparación null-safe
        ProductUnitModel a = ProductUnitModel.builder()
                .id(null)
                .code("C")
                .name("N")
                .description(null)
                .symbol(null)
                .enabled(null)
                .createdAt(null)
                .updatedAt(null)
                .createdBy(null)
                .updatedBy(null)
                .build();

        ProductUnitModel b = ProductUnitModel.builder()
                .id(null)
                .code("C")
                .name("N")
                .description(null)
                .symbol(null)
                .enabled(null)
                .createdAt(null)
                .updatedAt(null)
                .createdBy(null)
                .updatedBy(null)
                .build();

        assertEquals(a, b);                // mismos valores (incluyendo nulls)
        assertEquals(a.hashCode(), b.hashCode()); // hash consistente para iguales
    }

    @Test
    void equals_shouldBeFalse_whenOneFieldIsNullAndTheOtherIsNot() {
        // Idénticos salvo por 'description' (null vs no-null) para cubrir esa rama
        ProductUnitModel a = ProductUnitModel.builder()
                .id(UUID.randomUUID())
                .code("C")
                .name("N")
                .description(null)
                .symbol("kg")
                .enabled(true)
                .createdAt(null)
                .updatedAt(null)
                .createdBy("x")
                .updatedBy("y")
                .build();

        ProductUnitModel b = a.withDescription("Desc"); // sólo cambia este campo

        assertNotEquals(a, b);              // rama: null != no-null
    }

    @Test
    void hashCode_shouldBeConsistent_acrossMultipleInvocations() {
        ProductUnitModel a = full(UUID.randomUUID());
        int h1 = a.hashCode();
        int h2 = a.hashCode();
        assertEquals(h1, h2);               // consistencia
    }

    @Test
    void equals_nonNullAndDifferentValue_branch() {
        // Ambos no-nulos pero distintos en al menos un campo → false
        ProductUnitModel a = ProductUnitModel.builder().code("A").build();
        ProductUnitModel b = ProductUnitModel.builder().code("B").build();
        assertNotEquals(a, b);                   // rama: nonNull && !equals
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

    @Test
    void equals_differentType_branch() {
        ProductUnitModel a = full(UUID.randomUUID());
        assertFalse(a.equals("not-a-model"));    // !(o instanceof ProductUnitModel)
    }
}