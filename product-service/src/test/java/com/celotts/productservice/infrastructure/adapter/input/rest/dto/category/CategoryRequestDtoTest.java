package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category.CategoryRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Beverages");
        dto.setDescription("Soft drinks and juices");
        dto.setActive(true);
        dto.setCreatedBy("admin");
        dto.setUpdatedBy("admin2");

        assertEquals("Beverages", dto.getName());
        assertEquals("Soft drinks and juices", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("admin2", dto.getUpdatedBy());
    }

    @Test
    void testBuilderAndEqualsHashCode() {
        CategoryRequestDto dto1 = CategoryRequestDto.builder()
                .name("Snacks")
                .description("Salty snacks")
                .active(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        CategoryRequestDto dto2 = CategoryRequestDto.builder()
                .name("Snacks")
                .description("Salty snacks")
                .active(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CategoryRequestDto dto = CategoryRequestDto.builder()
                .name("Frozen")
                .description("Frozen foods")
                .active(false)
                .createdBy("user1")
                .updatedBy("user2")
                .build();

        String toString = dto.toString();
        assertTrue(toString.contains("Frozen"));
        assertTrue(toString.contains("Frozen foods"));
        assertTrue(toString.contains("user1"));
        assertTrue(toString.contains("user2"));
    }

    @Test
    void testValidation_NameIsBlank() {
        CategoryRequestDto dto = CategoryRequestDto.builder()
                .name(" ")
                .description("Test")
                .active(true)
                .build();

        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name is required")));
    }

    @Test
    void testValidation_NameTooShort() {
        CategoryRequestDto dto = CategoryRequestDto.builder()
                .name("A")
                .description("Test")
                .active(true)
                .build();

        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("between 2 and 100")));
    }

    @Test
    void testValidation_DescriptionTooLong() {
        String longDescription = "a".repeat(501);
        CategoryRequestDto dto = CategoryRequestDto.builder()
                .name("ValidName")
                .description(longDescription)
                .active(true)
                .build();

        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("cannot exceed 500 characters")));
    }
}