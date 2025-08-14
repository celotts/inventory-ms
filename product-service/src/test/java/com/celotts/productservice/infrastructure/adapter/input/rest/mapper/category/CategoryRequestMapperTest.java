package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productserviceOld.domain.model.CategoryModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.category.CategoryRequestMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRequestMapperTest {

    private final CategoryRequestMapper mapper = new CategoryRequestMapper();

    @Test
    void toModel_shouldMapCorrectly_whenAllFieldsAreProvided() {
        CategoryModel dto = CategoryModel.builder()
                .name("Tecnología")
                .description("Productos electrónicos")
                .active(false)
                .createdBy("admin")
                .build();

        CategoryModel result = mapper.toModel(dto);

        assertNotNull(result);
        assertEquals("Tecnología", result.getName());
        assertEquals("Productos electrónicos", result.getDescription());
        assertFalse(result.getActive());
        assertEquals("admin", result.getCreatedBy());
    }

    @Test
    void toModel_shouldApplyDefaults_whenActiveAndCreatedByAreNull() {
        CategoryModel dto = CategoryModel.builder()
                .name("Hogar")
                .description("Decoración")
                .active(null)
                .createdBy(null)
                .build();

        CategoryModel result = mapper.toModel(dto);

        assertNotNull(result);
        assertEquals("Hogar", result.getName());
        assertEquals("Decoración", result.getDescription());
        assertTrue(result.getActive()); // valor por defecto
        assertEquals("system", result.getCreatedBy()); // valor por defecto
    }

    @Test
    void updateModelFromDto_shouldUpdateFieldsCorrectly() {
        CategoryModel model = CategoryModel.builder()
                .name("Viejo nombre")
                .description("Vieja descripción")
                .active(true)
                .updatedBy("usuarioAnterior")
                .build();

        CategoryModel dto = CategoryModel.builder()
                .name("Nuevo nombre")
                .description("Nueva descripción")
                .active(false)
                .updatedBy("nuevoUsuario")
                .build();

        CategoryRequestMapper.updateModelFromDto(model, dto);

        assertEquals("Nuevo nombre", model.getName());
        assertEquals("Nueva descripción", model.getDescription());
        assertFalse(model.getActive());
        assertEquals("nuevoUsuario", model.getUpdatedBy());
    }

    @Test
    void updateModelFromDto_shouldAllowNullsInDto() {
        CategoryModel model = CategoryModel.builder()
                .name("Original")
                .description("Original")
                .active(true)
                .updatedBy("admin")
                .build();

        CategoryModel dto = CategoryModel.builder()
                .name(null)
                .description(null)
                .active(null)
                .updatedBy(null)
                .build();

        CategoryRequestMapper.updateModelFromDto(model, dto);

        assertNull(model.getName());
        assertNull(model.getDescription());
        assertNull(model.getActive());
        assertNull(model.getUpdatedBy());
    }
}