package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryEntityTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Test Category";
        String description = "Category description";
        Boolean active = false;
        String createdBy = "admin";
        String updatedBy = "user";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        CategoryEntity category = new CategoryEntity(
                id, name, description, active, createdBy, updatedBy, createdAt, updatedAt
        );

        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertFalse(category.getActive());
        assertEquals(createdBy, category.getCreatedBy());
        assertEquals(updatedBy, category.getUpdatedBy());
        assertEquals(createdAt, category.getCreatedAt());
        assertEquals(updatedAt, category.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CategoryEntity category = new CategoryEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        category.setId(id);
        category.setName("Categoria");
        category.setDescription("Descripción");
        category.setActive(true);
        category.setCreatedBy("root");
        category.setUpdatedBy("admin");
        category.setCreatedAt(createdAt);
        category.setUpdatedAt(updatedAt);

        assertEquals(id, category.getId());
        assertEquals("Categoria", category.getName());
        assertEquals("Descripción", category.getDescription());
        assertTrue(category.getActive());
        assertEquals("root", category.getCreatedBy());
        assertEquals("admin", category.getUpdatedBy());
        assertEquals(createdAt, category.getCreatedAt());
        assertEquals(updatedAt, category.getUpdatedAt());
    }

    @Test
    void testBuilder() {
        CategoryEntity category = CategoryEntity.builder()
                .name("Built Category")
                .description("desc")
                .build();

        assertEquals("Built Category", category.getName());
        assertEquals("desc", category.getDescription());
        assertTrue(category.getActive()); // Builder.Default
    }

    @Test
    void testToString() {
        CategoryEntity category = CategoryEntity.builder().name("Test").build();
        String result = category.toString();
        assertNotNull(result);
        assertTrue(result.contains("Test"));
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryEntity c1 = new CategoryEntity(id, "A", "desc", true, "admin", "admin", now, now);
        CategoryEntity c2 = new CategoryEntity(id, "A", "desc", true, "admin", "admin", now, now);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        CategoryEntity c1 = CategoryEntity.builder().name("One").build();
        CategoryEntity c2 = CategoryEntity.builder().name("Two").build();

        assertNotEquals(c1, c2);
    }

    @Test
    void testPrePersist_setsDefaultsIfNull() {
        CategoryEntity category = new CategoryEntity();
        category.prePersist();

        assertNotNull(category.getCreatedAt());
        assertTrue(category.getActive());
    }

    @Test
    void testPrePersist_doesNotOverrideExistingValues() {
        CategoryEntity category = new CategoryEntity();
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        category.setCreatedAt(now);
        category.setActive(false);

        category.prePersist(); // Should NOT override

        assertEquals(now, category.getCreatedAt());
        assertFalse(category.getActive());
    }

    @Test
    void testPreUpdate_setsUpdatedAt() {
        CategoryEntity category = new CategoryEntity();
        assertNull(category.getUpdatedAt());

        category.preUpdate();
        assertNotNull(category.getUpdatedAt());
    }
}