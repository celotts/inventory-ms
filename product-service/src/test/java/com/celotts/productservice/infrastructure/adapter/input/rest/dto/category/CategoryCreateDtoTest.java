package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCreateDtoTest {

    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        var dto = new CategoryCreateDto("Bebidas", "Categoría para bebidas frías");

        assertEquals("Bebidas", dto.getName());
        assertEquals("Categoría para bebidas frías", dto.getDescription());
    }

    @Test
    void testSetters() {
        var dto = new CategoryCreateDto();
        dto.setName("Comidas");
        dto.setDescription("Platillos preparados");

        assertEquals("Comidas", dto.getName());
        assertEquals("Platillos preparados", dto.getDescription());
    }

    @Test
    void testBuilder() {
        var dto = CategoryCreateDto.builder()
                .name("Postres")
                .description("Dulces y pasteles")
                .build();

        assertEquals("Postres", dto.getName());
        assertEquals("Dulces y pasteles", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        var dto1 = new CategoryCreateDto("Snacks", "Botanas saladas");
        var dto2 = new CategoryCreateDto("Snacks", "Botanas saladas");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        var dto = new CategoryCreateDto("Lácteos", "Leche y derivados");
        String result = dto.toString();

        assertTrue(result.contains("Lácteos"));
        assertTrue(result.contains("Leche y derivados"));
    }

    @Test
    void testValidation_NameBlank() {
        var dto = new CategoryCreateDto(" ", "Descripción válida");
        Set<ConstraintViolation<CategoryCreateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidation_DescriptionTooLong() {
        String longDescription = "a".repeat(501);
        var dto = new CategoryCreateDto("Carnes", longDescription);
        Set<ConstraintViolation<CategoryCreateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }
}