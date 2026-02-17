package com.celotts.productservice.domain.model.category;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CategoryModelTest {

    @Test
    void shouldCreateCategoryModelUsingBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryModel model = CategoryModel.builder()
                .id(id)
                .name("Electronics")
                .description("Gadgets")
                .active(true)
                .deleted(false)
                .createdBy("admin")
                .createdAt(now)
                .build();

        assertEquals(id, model.getId());
        assertEquals("Electronics", model.getName());
        assertTrue(model.getActive());
        assertFalse(model.isDeleted());
    }

    @Test
    void toString_shouldContainKeyFields() {
        CategoryModel model = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("TestCat")
                .active(true)
                .build();

        String str = model.toString();

        // Validamos que contenga los campos clave con los nombres actuales
        assertTrue(str.contains("id="));
        assertTrue(str.contains("name=TestCat"));
        assertTrue(str.contains("active=true")); // Antes fallaba porque buscaba "enabled="
    }

    @Test
    void update_shouldModifyFields() {
        CategoryModel model = CategoryModel.builder()
                .name("Old")
                .description("Old Desc")
                .active(false)
                .build();

        model.update("New", "New Desc", true, "user2");

        assertEquals("New", model.getName());
        assertEquals("New Desc", model.getDescription());
        assertTrue(model.getActive());
        assertEquals("user2", model.getUpdatedBy());
        assertNotNull(model.getUpdatedAt());
    }
}