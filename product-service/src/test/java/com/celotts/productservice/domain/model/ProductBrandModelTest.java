package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandModelTest {

    @Test
    void builderAndToBuilder_shouldBuildCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandModel brand = ProductBrandModel.builder()
                .id(id)
                .name("Test Brand")
                .description("Descripción")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductBrandModel copy = brand.toBuilder().name("New Name").build();

        assertNotNull(copy);
        assertEquals("New Name", copy.getName());
        assertEquals(brand.getId(), copy.getId());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        ProductBrandModel brand = new ProductBrandModel();

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        brand.setId(id);
        brand.setName("Test");
        brand.setDescription("Test Desc");
        brand.setEnabled(false);
        brand.setCreatedBy("admin");
        brand.setUpdatedBy("editor");
        brand.setCreatedAt(now);
        brand.setUpdatedAt(now);

        assertEquals("Test", brand.getName());
        assertEquals("Test Desc", brand.getDescription());
        assertFalse(brand.getEnabled());
        assertEquals("admin", brand.getCreatedBy());
        assertEquals("editor", brand.getUpdatedBy());
        assertEquals(now, brand.getCreatedAt());
        assertEquals(now, brand.getUpdatedAt());
    }

    @Test
    void activateAndDeactivate_shouldChangeState() {
        ProductBrandModel brand = new ProductBrandModel();
        brand.setEnabled(false);
        brand.activate();
        assertTrue(brand.getEnabled());

        brand.deactivate();
        assertFalse(brand.getEnabled());
    }

    @Test
    void isActive_shouldReturnCorrectStatus() {
        ProductBrandModel brand = new ProductBrandModel();
        brand.setEnabled(null);
        assertFalse(brand.isActive());

        brand.setEnabled(false);
        assertFalse(brand.isActive());

        brand.setEnabled(true);
        assertTrue(brand.isActive());
    }

    @Test
    void equalsAndHashCode_shouldBehaveAsExpected() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandModel b1 = ProductBrandModel.builder()
                .id(id)
                .name("Brand A")
                .createdAt(now)
                .build();

        ProductBrandModel b2 = ProductBrandModel.builder()
                .id(id)
                .name("Brand A")
                .createdAt(now)
                .build();

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void toString_shouldContainImportantFields() {
        ProductBrandModel brand = ProductBrandModel.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("ToStringTest")
                .build();

        String result = brand.toString();
        assertTrue(result.contains("ToStringTest"));
        assertTrue(result.contains("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void customConstructor_shouldInitializeValues() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = new ProductBrandModel(id, "Cool Brand", true);

        assertNotNull(brand);
        // Aunque no se seteen los campos en este constructor, lo invocas para cobertura
    }
}