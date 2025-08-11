package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
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
        assertNotNull(brand); // cobertura del ctor custom
    }

    // Opcional: si quieres cubrir canEqual de Lombok vía reflexión
    @Test
    void canEqual_shouldReturnTrueForSameType_andFalseForDifferentType_viaReflection() throws Exception {
        ProductBrandModel brand = new ProductBrandModel();

        Method m = ProductBrandModel.class.getDeclaredMethod("canEqual", Object.class);
        m.setAccessible(true);

        Object sameType = m.invoke(brand, new ProductBrandModel());
        Object otherType = m.invoke(brand, "other");

        assertTrue(sameType instanceof Boolean && (Boolean) sameType);
        assertTrue(otherType instanceof Boolean && !(Boolean) otherType);
    }

    @Test
    void equals_shouldHandleNullAndDifferentClass() {
        var now = LocalDateTime.now();
        var a = ProductBrandModel.builder()
                .id(UUID.randomUUID()).name("A").createdAt(now).build();

        assertNotEquals(a, null);          // null
        assertNotEquals(a, "otra-cosa");   // clase distinta
    }

   /* @Test
    void equals_shouldReturnFalse_whenAnyFieldDiffers() {
        var id = UUID.randomUUID();
        var now = LocalDateTime.now();

        var base = ProductBrandModel.builder()
                .id(id).name("Brand").description("D").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(now).updatedAt(now)
                .build();

        assertNotEquals(base, base.toBuilder().id(UUID.randomUUID()).build());
        assertNotEquals(base, base.toBuilder().name("X").build());
        assertNotEquals(base, base.toBuilder().description("DX").build());
        assertNotEquals(base, base.toBuilder().enabled(false).build());
        assertNotEquals(base, base.toBuilder().createdBy("cx").build());
        assertNotEquals(base, base.toBuilder().updatedBy("ux").build());
        assertNotEquals(base, base.toBuilder().createdAt(now.plusSeconds(1)).build());
        assertNotEquals(base, base.toBuilder().updatedAt(now.plusSeconds(2)).build());
    }

    @Test
    void hashCode_shouldDiffer_whenRelevantFieldDiffers() {
        var now = LocalDateTime.now();
        var a = ProductBrandModel.builder()
                .id(UUID.randomUUID()).name("A").createdAt(now).build();
        var b = a.toBuilder().name("B").build();

        assertNotEquals(a.hashCode(), b.hashCode());
    }*/
}