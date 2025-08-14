package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagUpdateDto; // <—
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagUpdateDtoValidationTest {

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

    private ProductTagUpdateDto baseDtoWithName(String name) {
        return ProductTagUpdateDto.builder()
                .name(name)
                .description("Desc opcional")
                .enabled(true)
                .updatedBy("tester")
                .build();
    }

    @Test
    void shouldFail_whenNameIsNull() {
        Set<ConstraintViolation<ProductTagUpdateDto>> v = validator.validate(baseDtoWithName(null));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank"));
    }

    @Test
    void shouldFail_whenNameIsBlank() {
        Set<ConstraintViolation<ProductTagUpdateDto>> v = validator.validate(baseDtoWithName("   "));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank"));
    }

    @Test
    void shouldFail_whenNameTooShort() {
        Set<ConstraintViolation<ProductTagUpdateDto>> v = validator.validate(baseDtoWithName("A")); // len=1
        // Puede saltar Size y/o Pattern; aceptamos cualquiera de las dos
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && (cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Size")
                || cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Pattern")));
    }

    @Test
    void shouldFail_whenNameTooLong() {
        String tooLong = "A".repeat(51); // > 50
        Set<ConstraintViolation<ProductTagUpdateDto>> v = validator.validate(baseDtoWithName(tooLong));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Size"));
    }

    @Test
    void shouldFail_whenNameHasInvalidCharacters() {
        Set<ConstraintViolation<ProductTagUpdateDto>> v = validator.validate(baseDtoWithName("Nombre$$"));
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("name")
                && cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Pattern"));
    }

    @Test
    void shouldPass_whenNameIsExactlyMinLengthAndValid() {
        Set<ConstraintViolation<ProductTagUpdateDto>> v = validator.validate(baseDtoWithName("Ab"));
        assertThat(v).isEmpty();
    }

    @Test
    void shouldPass_whenNameIsExactlyMaxLengthAndValid() {
        String maxLen = "A".repeat(50);
        Set<ConstraintViolation<ProductTagUpdateDto>> v = validator.validate(baseDtoWithName(maxLen));
        assertThat(v).isEmpty();
    }

    @Test
    void shouldPass_whenNameContainsAllowedCharsIncludingAccentsAndSymbols() {
        // Permitidos: letras, números, espacios, guion, guion_bajo y acentos
        Set<ConstraintViolation<ProductTagUpdateDto>> v =
                validator.validate(baseDtoWithName("Etiqueta_2025 - ÁÉÍÓÚñ"));
        assertThat(v).isEmpty();
    }
}