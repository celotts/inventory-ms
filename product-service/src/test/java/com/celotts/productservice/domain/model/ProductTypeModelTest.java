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
}