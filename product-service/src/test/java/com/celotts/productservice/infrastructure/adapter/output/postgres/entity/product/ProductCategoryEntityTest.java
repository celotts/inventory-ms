package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryEntityTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime assignedAt = LocalDateTime.now().minusDays(2);
        Boolean enabled = true;
        String createdBy = "admin";
        String updatedBy = "editor";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(5);
        LocalDateTime updatedAt = LocalDateTime.now();

        ProductCategoryEntity entity = ProductCategoryEntity.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(assignedAt)
                .enabled(enabled)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(productId, entity.getProductId());
        assertEquals(categoryId, entity.getCategoryId());
        assertEquals(assignedAt, entity.getAssignedAt());
        assertEquals(enabled, entity.getEnabled());
        assertEquals(createdBy, entity.getCreatedBy());
        assertEquals(updatedBy, entity.getUpdatedBy());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    void testSettersAndNoArgsConstructor() {
        ProductCategoryEntity entity = new ProductCategoryEntity();

        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setProductId(productId);
        entity.setCategoryId(categoryId);
        entity.setAssignedAt(now.minusDays(1));
        entity.setEnabled(false);
        entity.setCreatedBy("tester");
        entity.setUpdatedBy("tester2");
        entity.setCreatedAt(now.minusDays(5));
        entity.setUpdatedAt(now);

        assertEquals(id, entity.getId());
        assertEquals(productId, entity.getProductId());
        assertEquals(categoryId, entity.getCategoryId());
        assertEquals(now.minusDays(1), entity.getAssignedAt());
        assertFalse(entity.getEnabled());
        assertEquals("tester", entity.getCreatedBy());
        assertEquals("tester2", entity.getUpdatedBy());
        assertEquals(now.minusDays(5), entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();

        ProductCategoryEntity e1 = ProductCategoryEntity.builder()
                .id(id)
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        ProductCategoryEntity e2 = ProductCategoryEntity.builder()
                .id(id)
                .productId(e1.getProductId())
                .categoryId(e1.getCategoryId())
                .build();

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testNotEquals() {
        UUID id = UUID.randomUUID();

        ProductCategoryEntity e1 = ProductCategoryEntity.builder()
                .id(id)
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        ProductCategoryEntity e2 = ProductCategoryEntity.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        assertNotEquals(e1, e2);
    }

    @Test
    void testCanEqual() {
        ProductCategoryEntity e1 = new ProductCategoryEntity();
        ProductCategoryEntity e2 = new ProductCategoryEntity();

        assertTrue(e1.canEqual(e2));
        assertFalse(e1.canEqual("not-an-entity"));
    }

    @Test
    void testToString() {
        ProductCategoryEntity entity = ProductCategoryEntity.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .createdBy("creator")
                .build();

        String str = entity.toString();
        assertNotNull(str);
        assertTrue(str.contains("creator"));
    }
}