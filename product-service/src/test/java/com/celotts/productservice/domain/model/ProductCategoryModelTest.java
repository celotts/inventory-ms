package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryModelTest {

    @Test
    void builder_shouldCreateFullInstance() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductCategoryModel model = ProductCategoryModel.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(now)
                .enabled(true)
                .createdAt(now)
                .createdBy("admin")
                .active(true)
                .updatedBy("admin")
                .updatedAt(now)
                .build();

        assertNotNull(model);
        assertEquals(id, model.getId());
        assertEquals(productId, model.getProductId());
        assertEquals(categoryId, model.getCategoryId());
        assertEquals(now, model.getAssignedAt());
        assertTrue(model.getEnabled());
        assertEquals("admin", model.getCreatedBy());
        assertEquals("admin", model.getUpdatedBy());
        assertTrue(model.getActive());
        assertEquals(now, model.getCreatedAt());
        assertEquals(now, model.getUpdatedAt());
    }

    @Test
    void toBuilder_shouldCreateEqualCopy() {
        ProductCategoryModel original = ProductCategoryModel.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .enabled(true)
                .active(true)
                .createdBy("user1")
                .updatedBy("user1")
                .build();

        ProductCategoryModel copy = original.toBuilder().build();

        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void withMethod_shouldModifyField() {
        ProductCategoryModel original = ProductCategoryModel.builder()
                .createdBy("user1")
                .build();

        ProductCategoryModel modified = original.withCreatedBy("user2");

        assertEquals("user1", original.getCreatedBy());
        assertEquals("user2", modified.getCreatedBy());
    }

    @Test
    void equalsAndHashCode_shouldBehaveCorrectly() {
        UUID id = UUID.randomUUID();

        ProductCategoryModel m1 = ProductCategoryModel.builder()
                .id(id)
                .createdBy("user")
                .build();

        ProductCategoryModel m2 = ProductCategoryModel.builder()
                .id(id)
                .createdBy("user")
                .build();

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyInstance() {
        ProductCategoryModel model = new ProductCategoryModel();
        assertNotNull(model);
    }

    @Test
    void toString_shouldContainClassName() {
        ProductCategoryModel model = ProductCategoryModel.builder()
                .id(UUID.randomUUID())
                .build();

        String str = model.toString();
        assertTrue(str.contains("ProductCategoryModel"));
    }

    @Test
    void settersAndWithers_shouldSetAllFieldsCorrectly() {
        ProductCategoryModel model = new ProductCategoryModel();

        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        model.setId(id);
        model.setProductId(productId);
        model.setCategoryId(categoryId);
        model.setAssignedAt(now);
        model.setEnabled(true);
        model.setCreatedAt(now);
        model.setCreatedBy("creator");
        model.setActive(true);
        model.setUpdatedBy("updater");
        model.setUpdatedAt(now);

        assertEquals(id, model.getId());
        assertEquals(productId, model.getProductId());
        assertEquals(categoryId, model.getCategoryId());
        assertEquals(now, model.getAssignedAt());
        assertTrue(model.getEnabled());
        assertEquals(now, model.getCreatedAt());
        assertEquals("creator", model.getCreatedBy());
        assertTrue(model.getActive());
        assertEquals("updater", model.getUpdatedBy());
        assertEquals(now, model.getUpdatedAt());

        // probar withX methods también
        ProductCategoryModel copy = model
                .withId(UUID.randomUUID())
                .withProductId(UUID.randomUUID())
                .withCategoryId(UUID.randomUUID())
                .withAssignedAt(LocalDateTime.now())
                .withEnabled(false)
                .withCreatedAt(LocalDateTime.now())
                .withCreatedBy("newCreator")
                .withActive(false)
                .withUpdatedBy("newUpdater")
                .withUpdatedAt(LocalDateTime.now());

        assertNotEquals(model, copy); // asegurar que los cambios sean reales
    }
}