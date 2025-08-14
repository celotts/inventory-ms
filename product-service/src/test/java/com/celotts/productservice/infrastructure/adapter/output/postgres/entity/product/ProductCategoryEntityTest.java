package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryEntityTest {

    private ProductCategoryEntity base(UUID id) {
        return ProductCategoryEntity.builder()
                .id(id)
                .productId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .categoryId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .assignedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
                .enabled(true)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(LocalDateTime.of(2024, 1, 2, 3, 4, 6))
                .updatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 7))
                .build();
    }

    /** Clona todas las propiedades para modificar solo el campo que nos interesa en cada caso. */
    private ProductCategoryEntity copyOf(ProductCategoryEntity s) {
        return ProductCategoryEntity.builder()
                .id(s.getId())
                .productId(s.getProductId())
                .categoryId(s.getCategoryId())
                .assignedAt(s.getAssignedAt())
                .enabled(s.getEnabled())
                .createdBy(s.getCreatedBy())
                .updatedBy(s.getUpdatedBy())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    @Test
    void equals_sameInstance_true() {
        var a = base(UUID.randomUUID());
        assertEquals(a, a);
    }

    @Test
    void equals_null_false() {
        var a = base(UUID.randomUUID());
        assertNotEquals(a, null);
    }

    @Test
    void equals_differentClass_false() {
        var a = base(UUID.randomUUID());
        assertNotEquals(a, "not-an-entity");
    }

    @Test
    void equals_allFieldsEqual_true_andHashCodeEqual() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = base(id);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_differentId_false() {
        var a = base(UUID.randomUUID());
        var b = base(UUID.randomUUID()); // difiere id
        assertNotEquals(a, b);
    }

    @Test
    void equals_differentProductId_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setProductId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        assertNotEquals(a, b);
    }

    @Test
    void equals_differentCategoryId_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setCategoryId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
        assertNotEquals(a, b);
    }

    @Test
    void equals_differentAssignedAt_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setAssignedAt(a.getAssignedAt().plusSeconds(1));
        assertNotEquals(a, b);
    }

    @Test
    void equals_differentEnabled_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setEnabled(!a.getEnabled());
        assertNotEquals(a, b);
    }

    @Test
    void equals_withNullCreatedByOnOneSide_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setCreatedBy(null);
        assertNotEquals(a, b);
    }

    @Test
    void equals_withBothNullCreatedBy_true() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        a.setCreatedBy(null);
        b.setCreatedBy(null);
        assertEquals(a, b);
    }

    @Test
    void equals_withNullUpdatedByOnOneSide_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setUpdatedBy(null);
        assertNotEquals(a, b);
    }

    @Test
    void equals_withBothNullUpdatedBy_true() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        a.setUpdatedBy(null);
        b.setUpdatedBy(null);
        assertEquals(a, b);
    }

    @Test
    void equals_differentCreatedAt_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setCreatedAt(a.getCreatedAt().plusDays(1));
        assertNotEquals(a, b);
    }

    @Test
    void equals_differentUpdatedAt_false() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setUpdatedAt(a.getUpdatedAt().plusSeconds(1));
        assertNotEquals(a, b);
    }

    @Test
    void equals_symmetry_and_transitivity() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = base(id);
        var c = base(id);

        // simetría
        assertEquals(a, b);
        assertEquals(b, a);

        // transitividad
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    void hashCode_variesWhenFieldsChange() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = copyOf(a);
        b.setUpdatedAt(a.getUpdatedAt().plusSeconds(1));
        assertNotEquals(a.hashCode(), b.hashCode());

        // también al cambiar un campo nullable
        var c = copyOf(a);
        c.setCreatedBy(null);
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void toString_returnsNonNull_and_containsKeyFields() {
        UUID id = UUID.randomUUID();
        var e = ProductCategoryEntity.builder()
                .id(id)
                .productId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .categoryId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .assignedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
                .enabled(true)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(LocalDateTime.of(2024, 1, 2, 3, 4, 6))
                .updatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 7))
                .build();

        String s = e.toString();
        assertNotNull(s);
        // No exigimos formato exacto, solo pistas razonables:
        assertTrue(s.contains("ProductCategoryEntity"));
        assertTrue(s.contains(id.toString()));
        assertTrue(s.contains("creator"));
        assertTrue(s.contains("updater"));
    }

    @Test
    void defaultConstructor_and_setters_doNotExplode_and_toStringIsSafe() {
        var e = new ProductCategoryEntity(); // cubre el ctor por defecto
        // Con uno o dos setters basta para tocar el método y evitar NPEs en toString
        e.setId(UUID.randomUUID());
        e.setEnabled(Boolean.TRUE);

        // Debe ser seguro y no nulo aun con muchos nulls
        String s = e.toString();
        assertNotNull(s);
        assertTrue(s.contains("enabled=true"));
    }

    @Test
    void hashCode_handlesAllNullableNulls() {
        UUID id = UUID.randomUUID();
        var a = ProductCategoryEntity.builder()
                .id(id)
                .productId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .categoryId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .assignedAt(LocalDateTime.of(2024,1,2,3,4,5))
                .enabled(true)
                .createdBy(null)     // ambos null
                .updatedBy(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // No debe lanzar y debe ser estable
        int h1 = a.hashCode();
        int h2 = a.hashCode();
        assertEquals(h1, h2);
    }

    @Test
    void equals_allNullableNulls_true() {
        UUID id = UUID.randomUUID();
        var a = base(id);
        var b = base(id);

        a.setCreatedBy(null);
        a.setUpdatedBy(null);
        a.setCreatedAt(null);
        a.setUpdatedAt(null);

        b.setCreatedBy(null);
        b.setUpdatedBy(null);
        b.setCreatedAt(null);
        b.setUpdatedAt(null);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}