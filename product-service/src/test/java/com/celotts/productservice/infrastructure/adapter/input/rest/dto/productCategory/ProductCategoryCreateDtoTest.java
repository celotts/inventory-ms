package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryCreateDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void builder_shouldCreateObjectWithAllFields() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductCategoryCreateDto dto = ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(now)
                .enabled(true)
                .createdBy("tester")
                .updatedBy("updater")
                .build();

        assertEquals(productId, dto.getProductId());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals(now, dto.getAssignedAt());
        assertTrue(dto.getEnabled());
        assertEquals("tester", dto.getCreatedBy());
        assertEquals("updater", dto.getUpdatedBy());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductCategoryCreateDto dto1 = ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .enabled(true)
                .createdBy("tester")
                .build();

        ProductCategoryCreateDto dto2 = ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .enabled(true)
                .createdBy("tester")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndFields() {
        ProductCategoryCreateDto dto = ProductCategoryCreateDto.builder()
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .enabled(true)
                .createdBy("tester")
                .build();

        String toString = dto.toString();
        assertTrue(toString.contains("ProductCategoryCreateDto"));
        assertTrue(toString.contains("createdBy=tester"));
    }

    @Test
    void validation_shouldFailWhenRequiredFieldsAreNullOrBlank() {
        ProductCategoryCreateDto dto = ProductCategoryCreateDto.builder().build();
        Set<ConstraintViolation<ProductCategoryCreateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Product ID is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Category ID is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Enabled flag is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("createdBy is required")));
    }

    @Test
    void validation_shouldPassWhenAllRequiredFieldsAreSet() {
        ProductCategoryCreateDto dto = ProductCategoryCreateDto.builder()
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .enabled(true)
                .createdBy("tester")
                .build();

        Set<ConstraintViolation<ProductCategoryCreateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}