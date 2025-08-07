package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeModelTest {

    @Test
    void equals_shouldReturnTrue_whenComparingWithSameInstance() {
        ProductTypeModel model = ProductTypeModel.builder()
                .id(UUID.randomUUID())
                .name("Tipo A")
                .build();

        assertEquals(model, model); // referencia igual
    }

    @Test
    void equals_shouldReturnFalse_whenComparingWithNull() {
        ProductTypeModel model = ProductTypeModel.builder()
                .id(UUID.randomUUID())
                .name("Tipo A")
                .build();

        assertNotEquals(model, null); // comparación con null
    }

    @Test
    void equals_shouldReturnFalse_whenComparingWithDifferentClass() {
        ProductTypeModel model = ProductTypeModel.builder()
                .id(UUID.randomUUID())
                .name("Tipo A")
                .build();

        assertNotEquals(model, "otro objeto"); // clase diferente
    }

    @Test
    void equals_shouldReturnTrue_whenAllFieldsMatch() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeModel model1 = ProductTypeModel.builder()
                .id(id)
                .code("CODE")
                .name("Nombre")
                .descpiption("Descripción")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updateBy("admin")
                .build();

        ProductTypeModel model2 = ProductTypeModel.builder()
                .id(id)
                .code("CODE")
                .name("Nombre")
                .descpiption("Descripción")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updateBy("admin")
                .build();

        assertEquals(model1, model2); // todos los campos iguales
    }

    @Test
    void equals_shouldReturnFalse_whenAtLeastOneFieldDiffers() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeModel model1 = ProductTypeModel.builder()
                .id(id)
                .code("CODE1")
                .name("Nombre")
                .descpiption("Descripción")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updateBy("admin")
                .build();

        ProductTypeModel model2 = ProductTypeModel.builder()
                .id(id)
                .code("DIFFERENT_CODE")
                .name("Nombre")
                .descpiption("Descripción")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updateBy("admin")
                .build();

        assertNotEquals(model1, model2); // code diferente
    }

    @Test
    void hashCode_shouldBeSameForEqualObjects() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeModel model1 = ProductTypeModel.builder()
                .id(id)
                .code("CODE")
                .name("Tipo A")
                .descpiption("Desc")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updateBy("admin")
                .build();

        ProductTypeModel model2 = model1.toBuilder().build();

        assertEquals(model1.hashCode(), model2.hashCode());
    }

    @Test
    void toString_shouldIncludeKeyFields() {
        ProductTypeModel model = ProductTypeModel.builder()
                .code("ABC123")
                .name("Tipo Test")
                .build();

        String str = model.toString();

        assertTrue(str.contains("ABC123"));
        assertTrue(str.contains("Tipo Test"));
    }

    @Test
    void builder_shouldCreateObjectWithValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeModel model = ProductTypeModel.builder()
                .id(id)
                .code("PT01")
                .name("Nombre")
                .descpiption("Desc")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("creator")
                .updateBy("updater")
                .build();

        assertEquals("PT01", model.getCode());
        assertEquals("Nombre", model.getName());
        assertEquals("Desc", model.getDescpiption());
        assertEquals("creator", model.getCreatedBy());
        assertEquals("updater", model.getUpdateBy());
        assertTrue(model.getEnabled());
    }

    @Test
    void toBuilder_shouldCreateModifiedCopy() {
        ProductTypeModel original = ProductTypeModel.builder()
                .name("Original")
                .build();

        ProductTypeModel modified = original.toBuilder()
                .name("Modificado")
                .build();

        assertEquals("Original", original.getName());
        assertEquals("Modificado", modified.getName());
    }

    @Test
    void withMethods_shouldReturnModifiedCopies() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeModel model = ProductTypeModel.builder()
                .id(id)
                .code("CODE")
                .name("Nombre")
                .descpiption("Desc")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updateBy("editor")
                .build();

        ProductTypeModel modified = model
                .withId(UUID.randomUUID())
                .withCode("NEWCODE")
                .withName("NuevoNombre")
                .withDescpiption("NuevaDesc")
                .withEnabled(false)
                .withCreatedAt(now.minusDays(1))
                .withUpdatedAt(now.plusDays(1))
                .withCreatedBy("nuevo")
                .withUpdateBy("nuevoEditor");

        assertNotEquals(model, modified); // Verifica que no sean iguales
    }

    @Test
    void canEqual_shouldReturnFalseForDifferentClass() {
        ProductTypeModel model = ProductTypeModel.builder().code("C").build();
        assertFalse(model.canEqual("otro objeto"));
    }
}