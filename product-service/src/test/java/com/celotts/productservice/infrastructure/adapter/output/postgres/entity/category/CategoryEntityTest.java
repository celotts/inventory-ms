package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryEntityTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        CategoryEntity entity = new CategoryEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setName("Bebidas");
        entity.setDescription("Todo tipo de bebidas");
        entity.setActive(false);
        entity.setCreatedBy("admin");
        entity.setUpdatedBy("user1");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals(id, entity.getId());
        assertEquals("Bebidas", entity.getName());
        assertEquals("Todo tipo de bebidas", entity.getDescription());
        assertFalse(entity.getActive());
        assertEquals("admin", entity.getCreatedBy());
        assertEquals("user1", entity.getUpdatedBy());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        CategoryEntity entity = new CategoryEntity(
                id,
                "Snacks",
                "Comida rápida",
                true,
                "system",
                "editor",
                createdAt,
                updatedAt
        );

        assertEquals(id, entity.getId());
        assertEquals("Snacks", entity.getName());
        assertEquals("Comida rápida", entity.getDescription());
        assertTrue(entity.getActive());
        assertEquals("system", entity.getCreatedBy());
        assertEquals("editor", entity.getUpdatedBy());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    void testPrePersistSetsCreatedAtAndDefaultActive() {
        CategoryEntity entity = new CategoryEntity();
        entity.setActive(null);
        entity.setCreatedAt(null);

        entity.prePersist();

        assertNotNull(entity.getCreatedAt());
        assertTrue(entity.getActive());
    }

    @Test
    void testPreUpdateSetsUpdatedAt() {
        CategoryEntity entity = new CategoryEntity();
        entity.setUpdatedAt(null);

        entity.preUpdate();

        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testPrePersistSetsCreatedAtAndActiveWhenBothNull() {
        CategoryEntity entity = new CategoryEntity();
        entity.setCreatedAt(null);
        entity.setActive(null); // ⚠️ Necesario para cubrir esa línea

        entity.prePersist();

        assertNotNull(entity.getCreatedAt());
        assertTrue(entity.getActive()); // ⚠️ Esto cubre la línea resaltada
    }

    @Test
    void testPrePersist_WhenCreatedAtNotNull_ShouldNotChangeIt() {
        CategoryEntity entity = new CategoryEntity();
        LocalDateTime original = LocalDateTime.of(2023, 1, 1, 0, 0);
        entity.setCreatedAt(original); // valor NO null
        entity.setActive(null); // para no interferir en otra rama

        entity.prePersist();

        assertEquals(original, entity.getCreatedAt()); // se mantiene
    }

    @Test
    void testPrePersist_WhenActiveNotNull_ShouldNotChangeIt() {
        CategoryEntity entity = new CategoryEntity();
        entity.setCreatedAt(null); // para cubrir ese if
        entity.setActive(false); // valor NO null

        entity.prePersist();

        assertFalse(entity.getActive()); // no se sobreescribe a true
        assertNotNull(entity.getCreatedAt()); // se setea correctamente
    }
}