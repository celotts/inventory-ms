package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandCreateDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDto_shouldHaveNoViolations() {
        ProductBrandCreateDto dto = ProductBrandCreateDto.builder()
                .name("Test Brand")
                .description("A description of the brand")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        Set<ConstraintViolation<ProductBrandCreateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void testBlankFields_shouldTriggerValidationErrors() {
        ProductBrandCreateDto dto = ProductBrandCreateDto.builder()
                .name("")
                .description(" ")
                .enabled(null)
                .createdBy("")
                .updatedBy(null)
                .build();

        Set<ConstraintViolation<ProductBrandCreateDto>> violations = validator.validate(dto);
        assertEquals(5, violations.size(), "Expected 5 validation violations");
    }

    @Test
    void testNameTooLong_shouldFailValidation() {
        String longName = "A".repeat(101);
        ProductBrandCreateDto dto = ProductBrandCreateDto.builder()
                .name(longName)
                .description("Valid description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        Set<ConstraintViolation<ProductBrandCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testDescriptionTooLong_shouldFailValidation() {
        String longDescription = "B".repeat(501);
        ProductBrandCreateDto dto = ProductBrandCreateDto.builder()
                .name("Brand")
                .description(longDescription)
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        Set<ConstraintViolation<ProductBrandCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void testUpdatedByBlank_shouldFailValidation() {
        ProductBrandCreateDto dto = ProductBrandCreateDto.builder()
                .name("Brand")
                .description("Description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("   ") // solo espacios
                .build();

        Set<ConstraintViolation<ProductBrandCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("updatedBy")));
    }

    @Test
    void testSetters_shouldUpdateFieldsCorrectly() {
        ProductBrandCreateDto dto = new ProductBrandCreateDto();

        dto.setName("Test Name");
        dto.setDescription("Test Description");
        dto.setEnabled(true);
        dto.setCreatedBy("creator");
        dto.setUpdatedBy("updater");

        assertEquals("Test Name", dto.getName());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(true, dto.getEnabled());
        assertEquals("creator", dto.getCreatedBy());
        assertEquals("updater", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode_shouldWorkCorrectly() {
        ProductBrandCreateDto dto1 = ProductBrandCreateDto.builder()
                .name("Brand A")
                .description("Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ProductBrandCreateDto dto2 = ProductBrandCreateDto.builder()
                .name("Brand A")
                .description("Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString_shouldContainClassNameAndFields() {
        ProductBrandCreateDto dto = ProductBrandCreateDto.builder()
                .name("Brand A")
                .description("Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        String toString = dto.toString();
        assertTrue(toString.contains("Brand A"));
        assertTrue(toString.contains("Desc"));
        assertTrue(toString.contains("admin"));
        assertTrue(toString.contains("enabled=true"));
    }

    @Test
    void builder_toString_shouldIncludeFields() {
        // No hacemos build(), queremos cubrir el toString() del BUILDER
        var builder = ProductBrandCreateDto.builder()
                .name("Nike")
                .description("Sports brand")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor");

        String s = builder.toString();

        assertNotNull(s);
        assertTrue(s.contains("Nike"));
        assertTrue(s.contains("Sports brand"));
        assertTrue(s.contains("enabled=true"));
        assertTrue(s.contains("admin"));
        assertTrue(s.contains("editor"));
    }

    @Test
    void builder_build_getters_shouldWork() {
        var dto = ProductBrandCreateDto.builder()
                .name("Adidas")
                .description("Shoes")
                .enabled(false)
                .createdBy("user1")
                .updatedBy("user2")
                .build();

        assertEquals("Adidas", dto.getName());
        assertEquals("Shoes", dto.getDescription());
        assertFalse(dto.getEnabled());
        assertEquals("user1", dto.getCreatedBy());
        assertEquals("user2", dto.getUpdatedBy());
    }
}