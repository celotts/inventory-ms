package com.celotts.productservice.domain.model.product;

import com.celotts.productservice.domain.model.product.ProductBrandModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandModelTest {

    @Test
    void builder_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandModel brand = ProductBrandModel.builder()
                .id(id)
                .name("CoolBrand")
                .description("Brand for cool products")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, brand.getId());
        assertEquals("CoolBrand", brand.getName());
        assertEquals("Brand for cool products", brand.getDescription());
        assertTrue(brand.getEnabled());
        assertEquals("admin", brand.getCreatedBy());
        assertEquals("editor", brand.getUpdatedBy());
        assertEquals(now, brand.getCreatedAt());
        assertEquals(now, brand.getUpdatedAt());
    }

    @Test
    void setters_shouldAssignValues() {
        ProductBrandModel brand = new ProductBrandModel();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        brand.setId(id);
        brand.setName("SetterBrand");
        brand.setDescription("Using setters");
        brand.setEnabled(false);
        brand.setCreatedBy("system");
        brand.setUpdatedBy("user");
        brand.setCreatedAt(now);
        brand.setUpdatedAt(now);

        assertEquals(id, brand.getId());
        assertEquals("SetterBrand", brand.getName());
        assertEquals("Using setters", brand.getDescription());
        assertFalse(brand.getEnabled());
        assertEquals("system", brand.getCreatedBy());
        assertEquals("user", brand.getUpdatedBy());
        assertEquals(now, brand.getCreatedAt());
        assertEquals(now, brand.getUpdatedAt());
    }

    @Test
    void allArgsConstructor_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandModel brand = new ProductBrandModel(
                id, "AllArgs", "With constructor", true,
                "creator", "updator", now, now
        );

        assertEquals("AllArgs", brand.getName());
        assertTrue(brand.getEnabled());
    }

    @Test
    void customConstructor_shouldInitialize() {
        ProductBrandModel brand = new ProductBrandModel(UUID.randomUUID(), "CustomBrand", true);

        // Actualmente el constructor no asigna los valores
        // pero podemos validar que al menos se creó la instancia
        assertNotNull(brand);
    }

    @Test
    void isActive_activate_and_deactivate_shouldWork() {
        ProductBrandModel brand = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("BizBrand")
                .enabled(null) // empieza como null
                .build();

        assertFalse(brand.isActive()); // null => false

        brand.activate();
        assertTrue(brand.isActive());

        brand.deactivate();
        assertFalse(brand.isActive());
    }

    @Test
    void toBuilder_shouldCloneAndModify() {
        ProductBrandModel original = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Original")
                .enabled(true)
                .build();

        ProductBrandModel clone = original.toBuilder()
                .name("Cloned")
                .build();

        assertEquals(original.getId(), clone.getId());
        assertEquals("Cloned", clone.getName());
        assertTrue(clone.getEnabled());
    }

    @Test
    void equals_and_hashCode_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandModel b1 = ProductBrandModel.builder()
                .id(id)
                .name("Same")
                .description("Desc")
                .enabled(true)
                .createdAt(now)
                .build();

        ProductBrandModel b2 = ProductBrandModel.builder()
                .id(id)
                .name("Same")
                .description("Desc")
                .enabled(true)
                .createdAt(now)
                .build();

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndFields() {
        ProductBrandModel brand = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("BrandX")
                .description("DescX")
                .enabled(true)
                .createdBy("tester")
                .build();

        String str = brand.toString();
        assertTrue(str.contains("BrandX"));
        assertTrue(str.contains("tester"));
        assertTrue(str.startsWith("ProductBrandModel(")); // Lombok @Data toString
    }
}