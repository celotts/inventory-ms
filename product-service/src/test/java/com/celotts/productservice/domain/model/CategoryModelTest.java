package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryModelTest {

    @Test
    void shouldBuildAndUpdateCategory() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryModel category = CategoryModel.builder()
                .id(id)
                .name("Bebidas")
                .description("Líquidos")
                .active(true)
                .createdBy("admin")
                .createdAt(now)
                .deleted(false)
                .build();

        assertEquals("Bebidas", category.getName());
        assertEquals("Líquidos", category.getDescription());
        assertTrue(category.getActive());
        assertEquals("admin", category.getCreatedBy());
        assertEquals(now, category.getCreatedAt());
        assertFalse(category.isDeleted());

        // Ejecutar update()
        category.update("Snacks", "Comida rápida", false, "editor");

        assertEquals("Snacks", category.getName());
        assertEquals("Comida rápida", category.getDescription());
        assertFalse(category.getActive());
        assertEquals("editor", category.getUpdatedBy());
        assertNotNull(category.getUpdatedAt());
    }

    @Test
    void shouldUseNoArgsConstructorAndSetters() {
        CategoryModel category = new CategoryModel();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        category.setId(id);
        category.setName("Lácteos");
        category.setDescription("Productos derivados de la leche");
        category.setActive(true);
        category.setCreatedBy("admin");
        category.setCreatedAt(now);
        category.setUpdatedBy("admin");
        category.setUpdatedAt(now);
        category.setDeleted(true);

        assertEquals(id, category.getId());
        assertEquals("Lácteos", category.getName());
        assertEquals("Productos derivados de la leche", category.getDescription());
        assertTrue(category.getActive());
        assertEquals("admin", category.getCreatedBy());
        assertEquals(now, category.getCreatedAt());
        assertEquals("admin", category.getUpdatedBy());
        assertEquals(now, category.getUpdatedAt());
        assertTrue(category.isDeleted());
    }
}