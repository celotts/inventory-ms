package com.celotts.productservice.domain.model;

import com.celotts.productserviceOld.domain.model.CategoryModel;
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
    void shouldUseAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryModel category = new CategoryModel(
                id, "Enlatados", "Comida en lata", true, "admin", "admin", now, now, false
        );

        assertEquals(id, category.getId());
        assertEquals("Enlatados", category.getName());
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

    @Test
    void shouldPrintToString() {
        CategoryModel category = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Snacks")
                .description("Algo rico")
                .active(true)
                .createdBy("tester")
                .createdAt(LocalDateTime.now())
                .build();

        String output = category.toString();
        assertNotNull(output); // Verifica que no sea null
        assertTrue(output.contains("Snacks")); // Verifica contenido
    }

    @Test
    void shouldCallToString() {
        CategoryModel category = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Snacks")
                .description("Comida rápida")
                .active(true)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();

        String result = category.toString();

        assertNotNull(result);                  // Verifica que no sea null
        assertTrue(result.contains("Snacks"));  // Verifica contenido esperado
    }

    @Test
    void shouldCoverToStringCompletely() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        LocalDateTime now = LocalDateTime.of(2023, 8, 7, 12, 0);

        CategoryModel category = CategoryModel.builder()
                .id(id)
                .name("Snacks")
                .description("Comida rápida")
                .active(false)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(now)
                .updatedAt(now)
                .deleted(true)
                .build();

        String expected = "CategoryModel{" +
                "id=" + id +
                ", name='Snacks'" +
                ", description='Comida rápida'" +
                ", active=false" +
                ", createdBy='admin'" +
                ", updatedBy='editor'" +
                ", createdAt=" + now +
                ", updatedAt=" + now +
                ", deleted=true" +
                '}';

        assertEquals(expected, category.toString());
    }

    @Test
    void shouldCoverToStringWithNullFields() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        CategoryModel category = CategoryModel.builder()
                .id(id)
                .name(null)
                .description(null)
                .active(null)
                .createdBy(null)
                .updatedBy(null)
                .createdAt(null)
                .updatedAt(null)
                .deleted(false)
                .build();

        String result = category.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("name='null'")); // Lombok lo imprimirá así
    }
}