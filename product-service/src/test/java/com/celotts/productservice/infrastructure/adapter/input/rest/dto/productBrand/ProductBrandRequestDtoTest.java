package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDto_shouldHaveNoViolations() {
        ProductBrandRequestDto dto = ProductBrandRequestDto.builder()
                .name("Valid Brand")
                .description("Valid description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        Set<ConstraintViolation<ProductBrandRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void testBlankName_shouldFailValidation() {
        ProductBrandRequestDto dto = ProductBrandRequestDto.builder()
                .name(" ")
                .description("Valid description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        Set<ConstraintViolation<ProductBrandRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testNameTooShort_shouldFailValidation() {
        ProductBrandRequestDto dto = ProductBrandRequestDto.builder()
                .name("A")
                .description("Valid description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        Set<ConstraintViolation<ProductBrandRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testNameTooLong_shouldFailValidation() {
        String longName = "A".repeat(101);
        ProductBrandRequestDto dto = ProductBrandRequestDto.builder()
                .name(longName)
                .description("Valid description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        Set<ConstraintViolation<ProductBrandRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testDescriptionTooLong_shouldFailValidation() {
        String longDescription = "D".repeat(501);
        ProductBrandRequestDto dto = ProductBrandRequestDto.builder()
                .name("Brand")
                .description(longDescription)
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        Set<ConstraintViolation<ProductBrandRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void testNullEnabled_shouldFailValidation() {
        ProductBrandRequestDto dto = ProductBrandRequestDto.builder()
                .name("Brand")
                .description("Valid description")
                .enabled(null)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        Set<ConstraintViolation<ProductBrandRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("enabled")));
    }

    @Test
    void testGettersSettersEqualsHashCodeToString() {
        ProductBrandRequestDto dto1 = new ProductBrandRequestDto();
        dto1.setName("Brand A");
        dto1.setDescription("Some description");
        dto1.setEnabled(true);
        dto1.setCreatedBy("creator");
        dto1.setUpdatedBy("updater");

        assertEquals("Brand A", dto1.getName());
        assertEquals("Some description", dto1.getDescription());
        assertTrue(dto1.getEnabled());
        assertEquals("creator", dto1.getCreatedBy());
        assertEquals("updater", dto1.getUpdatedBy());

        // Probar toString no sea null y contenga campos importantes
        String toString = dto1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Brand A"));
        assertTrue(toString.contains("Some description"));

        // Probar equals y hashCode
        ProductBrandRequestDto dto2 = new ProductBrandRequestDto(
                "Brand A", "Some description", true, "creator", "updater"
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}