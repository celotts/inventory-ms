package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag;


import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagCreateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagCreateDtoValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) factory.close();
    }

    private ProductTagCreateDto baseDto(String name, String createdBy) {
        return ProductTagCreateDto.builder()
                .name(name)
                .description("Desc opcional")
                // no seteamos enabled a propósito para comprobar @Builder.Default
                .createdBy(createdBy)
                .build();
    }

    @Test
    void builder_shouldApplyDefaultEnabledTrue() {
        ProductTagCreateDto dto = baseDto("Etiqueta Válida", "user1");
        assertThat(dto.getEnabled()).isTrue(); // viene de @Builder.Default
    }

    @Test
    void shouldPass_whenAllValid() {
        Set<ConstraintViolation<ProductTagCreateDto>> v =
                validator.validate(baseDto("Nombre Correcto-2025", "creator"));
        assertThat(v).isEmpty();
    }

    @Test
    void shouldFail_whenNameIsNull() {
        Set<ConstraintViolation<ProductTagCreateDto>> v = validator.validate(baseDto(null, "creator"));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank"));
    }

    @Test
    void shouldFail_whenNameIsBlank() {
        Set<ConstraintViolation<ProductTagCreateDto>> v = validator.validate(baseDto("   ", "creator"));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank"));
    }

    @Test
    void shouldFail_whenNameTooShort() {
        Set<ConstraintViolation<ProductTagCreateDto>> v = validator.validate(baseDto("A", "creator"));
        // Puede disparar Size y/o Pattern. Aceptamos cualquiera.
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && (cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Size")
                || cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Pattern")));
    }

    @Test
    void shouldFail_whenNameTooLong() {
        String longName = "A".repeat(51);
        Set<ConstraintViolation<ProductTagCreateDto>> v = validator.validate(baseDto(longName, "creator"));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Size"));
    }

    @Test
    void shouldFail_whenNameHasInvalidChars() {
        Set<ConstraintViolation<ProductTagCreateDto>> v = validator.validate(baseDto("Nombre$$", "creator"));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Pattern"));
    }

    @Test
    void shouldFail_whenCreatedByIsNull() {
        Set<ConstraintViolation<ProductTagCreateDto>> v = validator.validate(baseDto("Nombre", null));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("createdBy")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank"));
    }

    @Test
    void shouldFail_whenCreatedByIsBlank() {
        Set<ConstraintViolation<ProductTagCreateDto>> v = validator.validate(baseDto("Nombre", "  "));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("createdBy")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank"));
    }

    @Test
    void shouldPass_whenNameContainsAllowedChars() {
        Set<ConstraintViolation<ProductTagCreateDto>> v =
                validator.validate(baseDto("Etiqueta_2025 - ÁÉÍÓÚñ", "creator"));
        assertThat(v).isEmpty();
    }
}