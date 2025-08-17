package com.celotts.productservice.domain.model.category;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryModelTest {

    @Test
    void builder_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryModel category = CategoryModel.builder()
                .id(id)
                .name("Bebidas")
                .description("Refrescos y jugos")
                .active(true)
                .createdBy("admin")
                .updatedBy("admin")
                .createdAt(now)
                .updatedAt(now)
                .deleted(false)
                .build();

        assertEquals(id, category.getId());
        assertEquals("Bebidas", category.getName());
        assertEquals("Refrescos y jugos", category.getDescription());
        assertTrue(category.getActive());
        assertEquals("admin", category.getCreatedBy());
        assertEquals("admin", category.getUpdatedBy());
        assertEquals(now, category.getCreatedAt());
        assertEquals(now, category.getUpdatedAt());
        assertFalse(category.isDeleted());
    }

    @Test
    void setters_shouldUpdateValues() {
        CategoryModel category = new CategoryModel();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        category.setId(id);
        category.setName("Botanas");
        category.setDescription("Snacks y frituras");
        category.setActive(false);
        category.setCreatedBy("system");
        category.setUpdatedBy("tester");
        category.setCreatedAt(now.minusDays(1));
        category.setUpdatedAt(now);
        category.setDeleted(true);

        assertEquals(id, category.getId());
        assertEquals("Botanas", category.getName());
        assertEquals("Snacks y frituras", category.getDescription());
        assertFalse(category.getActive());
        assertEquals("system", category.getCreatedBy());
        assertEquals("tester", category.getUpdatedBy());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertTrue(category.isDeleted());
    }

    @Test
    void update_shouldChangeFieldsAndUpdateUpdatedAt() throws InterruptedException {
        CategoryModel category = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Viejo")
                .description("Desc vieja")
                .active(false)
                .updatedBy("oldUser")
                .updatedAt(LocalDateTime.now().minusMinutes(5))
                .build();

        LocalDateTime beforeUpdate = category.getUpdatedAt();
        Thread.sleep(5); // asegurar diferencia en tiempo

        category.update("Nuevo", "Desc nueva", true, "newUser");

        assertEquals("Nuevo", category.getName());
        assertEquals("Desc nueva", category.getDescription());
        assertTrue(category.getActive());
        assertEquals("newUser", category.getUpdatedBy());
        assertTrue(category.getUpdatedAt().isAfter(beforeUpdate));
    }

    @Test
    void toBuilder_shouldCloneAndModify() {
        CategoryModel original = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Original")
                .active(true)
                .build();

        CategoryModel clone = original.toBuilder()
                .name("Clonado")
                .build();

        assertEquals(original.getId(), clone.getId()); // conserva id
        assertEquals("Clonado", clone.getName());
        assertTrue(clone.getActive());
    }

    @Test
    void toString_shouldContainKeyFields() {
        CategoryModel category = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Bebidas")
                .description("Algo")
                .active(true)
                .createdBy("user1")
                .updatedBy("user2")
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 2, 11, 0))
                .deleted(false)
                .build();

        String result = category.toString();
        assertTrue(result.contains("Bebidas"));
        assertTrue(result.contains("user1"));
        assertTrue(result.contains("user2"));
        assertTrue(result.startsWith("CategoryModel{"));
    }
}