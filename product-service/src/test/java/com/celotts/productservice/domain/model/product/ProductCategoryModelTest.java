package com.celotts.productservice.domain.model.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryModelTest {

    @Test
    void builder_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductCategoryModel m = ProductCategoryModel.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(now.minusDays(1))
                .enabled(true)
                .createdAt(now.minusDays(2))
                .createdBy("creator")
                .active(true)
                .updatedBy("updater")
                .updatedAt(now)
                .build();

        assertEquals(id, m.getId());
        assertEquals(productId, m.getProductId());
        assertEquals(categoryId, m.getCategoryId());
        assertEquals(now.minusDays(1), m.getAssignedAt());
        assertTrue(m.getEnabled());
        assertEquals(now.minusDays(2), m.getCreatedAt());
        assertEquals("creator", m.getCreatedBy());
        assertTrue(m.getActive());
        assertEquals("updater", m.getUpdatedBy());
        assertEquals(now, m.getUpdatedAt());
    }

    @Test
    void setters_shouldAssignValues() {
        ProductCategoryModel m = new ProductCategoryModel();
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        m.setId(id);
        m.setProductId(productId);
        m.setCategoryId(categoryId);
        m.setAssignedAt(now.minusHours(3));
        m.setEnabled(false);
        m.setCreatedAt(now.minusDays(5));
        m.setCreatedBy("system");
        m.setActive(false);
        m.setUpdatedBy("tester");
        m.setUpdatedAt(now);

        assertEquals(id, m.getId());
        assertEquals(productId, m.getProductId());
        assertEquals(categoryId, m.getCategoryId());
        assertEquals(now.minusHours(3), m.getAssignedAt());
        assertFalse(m.getEnabled());
        assertEquals(now.minusDays(5), m.getCreatedAt());
        assertEquals("system", m.getCreatedBy());
        assertFalse(m.getActive());
        assertEquals("tester", m.getUpdatedBy());
        assertEquals(now, m.getUpdatedAt());
    }

    @Test
    void allArgsConstructor_shouldWork() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime assigned = LocalDateTime.now().minusDays(1);
        LocalDateTime created = LocalDateTime.now().minusDays(2);
        LocalDateTime updated = LocalDateTime.now();

        ProductCategoryModel m = new ProductCategoryModel(
                id, productId, categoryId, assigned, true, created, "creator", true, "upd", updated
        );

        assertEquals(id, m.getId());
        assertEquals(productId, m.getProductId());
        assertEquals(categoryId, m.getCategoryId());
        assertEquals(assigned, m.getAssignedAt());
        assertTrue(m.getEnabled());
        assertEquals(created, m.getCreatedAt());
        assertEquals("creator", m.getCreatedBy());
        assertTrue(m.getActive());
        assertEquals("upd", m.getUpdatedBy());
        assertEquals(updated, m.getUpdatedAt());
    }

    @Test
    void withers_shouldCreateModifiedCopies() {
        ProductCategoryModel base = ProductCategoryModel.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .enabled(false)
                .active(false)
                .createdBy("a")
                .updatedBy("b")
                .build();

        ProductCategoryModel modified = base
                .withEnabled(true)
                .withActive(true)
                .withUpdatedBy("c");

        // El with no debe mutar el original
        assertFalse(base.getEnabled());
        assertFalse(base.getActive());
        assertEquals("b", base.getUpdatedBy());

        // Pero sí debe reflejar cambios en la copia
        assertTrue(modified.getEnabled());
        assertTrue(modified.getActive());
        assertEquals("c", modified.getUpdatedBy());

        // Mismo id/product/category (no cambiamos esos campos)
        assertEquals(base.getId(), modified.getId());
        assertEquals(base.getProductId(), modified.getProductId());
        assertEquals(base.getCategoryId(), modified.getCategoryId());
    }

    @Test
    void toBuilder_shouldCloneAndModify() {
        ProductCategoryModel original = ProductCategoryModel.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .enabled(true)
                .active(true)
                .createdBy("x")
                .updatedBy("y")
                .build();

        ProductCategoryModel clone = original.toBuilder()
                .enabled(false)
                .updatedBy("z")
                .build();

        // Conserva campos no modificados
        assertEquals(original.getId(), clone.getId());
        assertEquals(original.getProductId(), clone.getProductId());
        assertEquals(original.getCategoryId(), clone.getCategoryId());
        assertEquals(original.getCreatedBy(), clone.getCreatedBy());
        assertEquals(original.getActive(), clone.getActive());

        // Cambios aplicados
        assertFalse(clone.getEnabled());
        assertEquals("z", clone.getUpdatedBy());
    }

    @Test
    void equals_and_hashCode_shouldWork() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductCategoryModel a = ProductCategoryModel.builder()
                .id(id).productId(productId).categoryId(categoryId).enabled(true).build();

        ProductCategoryModel b = ProductCategoryModel.builder()
                .id(id).productId(productId).categoryId(categoryId).enabled(true).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndSomeFields() {
        ProductCategoryModel m = ProductCategoryModel.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .createdBy("tester")
                .enabled(true)
                .build();

        String s = m.toString();
        assertTrue(s.contains("ProductCategoryModel"));
        assertTrue(s.contains("tester"));
        assertTrue(s.contains("enabled=true"));
    }
}