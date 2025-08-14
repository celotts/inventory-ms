package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandUpdateDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDto_shouldHaveNoViolations() {
        ProductBrandUpdateDto dto = ProductBrandUpdateDto.builder()
                .name("Updated Brand")
                .description("Updated description")
                .enabled(false)
                .updatedBy("user123")
                .build();

        Set<ConstraintViolation<ProductBrandUpdateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void testBlankFields_shouldTriggerValidationErrors() {
        ProductBrandUpdateDto dto = ProductBrandUpdateDto.builder()
                .name("")
                .description("")
                .enabled(null)
                .updatedBy("")
                .build();

        Set<ConstraintViolation<ProductBrandUpdateDto>> violations = validator.validate(dto);
        assertEquals(4, violations.size(), "Expected 4 validation violations");
    }

    @Test
    void testNameTooLong_shouldFailValidation() {
        String longName = "X".repeat(101);
        ProductBrandUpdateDto dto = ProductBrandUpdateDto.builder()
                .name(longName)
                .description("Valid description")
                .enabled(true)
                .updatedBy("admin")
                .build();

        Set<ConstraintViolation<ProductBrandUpdateDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testDescriptionTooLong_shouldFailValidation() {
        String longDesc = "D".repeat(501);
        ProductBrandUpdateDto dto = ProductBrandUpdateDto.builder()
                .name("Brand")
                .description(longDesc)
                .enabled(true)
                .updatedBy("admin")
                .build();

        Set<ConstraintViolation<ProductBrandUpdateDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void testUpdatedByBlank_shouldFailValidation() {
        ProductBrandUpdateDto dto = ProductBrandUpdateDto.builder()
                .name("Brand")
                .description("Valid")
                .enabled(true)
                .updatedBy("   ") // solo espacios
                .build();

        Set<ConstraintViolation<ProductBrandUpdateDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("updatedBy")));
    }

    @Test
    void testSettersAndGetters_shouldWorkCorrectly() {
        ProductBrandUpdateDto dto = new ProductBrandUpdateDto();
        dto.setName("BrandX");
        dto.setDescription("DescX");
        dto.setEnabled(false);
        dto.setUpdatedBy("tester");

        assertEquals("BrandX", dto.getName());
        assertEquals("DescX", dto.getDescription());
        assertFalse(dto.getEnabled());
        assertEquals("tester", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode_shouldBeEqual() {
        ProductBrandUpdateDto dto1 = ProductBrandUpdateDto.builder()
                .name("BrandX")
                .description("DescX")
                .enabled(true)
                .updatedBy("admin")
                .build();

        ProductBrandUpdateDto dto2 = ProductBrandUpdateDto.builder()
                .name("BrandX")
                .description("DescX")
                .enabled(true)
                .updatedBy("admin")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString_shouldContainFields() {
        ProductBrandUpdateDto dto = ProductBrandUpdateDto.builder()
                .name("BrandX")
                .description("DescX")
                .enabled(true)
                .updatedBy("admin")
                .build();

        String str = dto.toString();
        assertTrue(str.contains("BrandX"));
        assertTrue(str.contains("DescX"));
        assertTrue(str.contains("admin"));
    }
}