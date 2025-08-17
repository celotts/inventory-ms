package com.celotts.productservice.domain.model.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTagModelTest {

    @Test
    void builder_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTagModel tag = ProductTagModel.builder()
                .id(id)
                .name("Electronics")
                .description("Electronic gadgets")
                .enabled(true)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(now.minusDays(2))
                .updatedAt(now)
                .build();

        assertEquals(id, tag.getId());
        assertEquals("Electronics", tag.getName());
        assertEquals("Electronic gadgets", tag.getDescription());
        assertTrue(tag.getEnabled());
        assertEquals("creator", tag.getCreatedBy());
        assertEquals("updater", tag.getUpdatedBy());
        assertEquals(now.minusDays(2), tag.getCreatedAt());
        assertEquals(now, tag.getUpdatedAt());
    }

    @Test
    void withEnabled_shouldReturnModifiedCopy() {
        ProductTagModel tag = ProductTagModel.builder()
                .id(UUID.randomUUID())
                .name("Books")
                .enabled(false)
                .build();

        ProductTagModel enabledTag = tag.withEnabled(true);

        // Original no cambia
        assertFalse(tag.getEnabled());
        // Copia refleja el cambio
        assertTrue(enabledTag.getEnabled());
        // Mismo id y nombre
        assertEquals(tag.getId(), enabledTag.getId());
        assertEquals("Books", enabledTag.getName());
    }

    @Test
    void toBuilder_shouldCloneAndModify() {
        ProductTagModel original = ProductTagModel.builder()
                .id(UUID.randomUUID())
                .name("Clothes")
                .enabled(true)
                .createdBy("admin")
                .build();

        ProductTagModel modified = original.toBuilder()
                .name("Shoes")
                .build();

        // Conserva lo que no cambiamos
        assertEquals(original.getId(), modified.getId());
        assertEquals(original.getCreatedBy(), modified.getCreatedBy());
        assertEquals(original.getEnabled(), modified.getEnabled());

        // Se aplican cambios
        assertEquals("Shoes", modified.getName());
    }

    @Test
    void equals_and_hashCode_shouldWork() {
        UUID id = UUID.randomUUID();

        ProductTagModel t1 = ProductTagModel.builder()
                .id(id)
                .name("Same")
                .enabled(true)
                .build();

        ProductTagModel t2 = ProductTagModel.builder()
                .id(id)
                .name("Same")
                .enabled(true)
                .build();

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndFields() {
        ProductTagModel tag = ProductTagModel.builder()
                .id(UUID.randomUUID())
                .name("Kitchen")
                .enabled(true)
                .createdBy("tester")
                .build();

        String str = tag.toString();
        assertTrue(str.contains("ProductTagModel"));
        assertTrue(str.contains("Kitchen"));
        assertTrue(str.contains("tester"));
    }
}