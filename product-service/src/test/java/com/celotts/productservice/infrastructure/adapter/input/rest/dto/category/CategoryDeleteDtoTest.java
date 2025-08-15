package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryDeleteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDeleteDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void noArgsConstructor_shouldCreateObject() {
        CategoryDeleteDto dto = new CategoryDeleteDto();
        assertNull(dto.getId());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        UUID id = UUID.randomUUID();
        CategoryDeleteDto dto = new CategoryDeleteDto(id);
        assertEquals(id, dto.getId());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        UUID id = UUID.randomUUID();
        CategoryDeleteDto dto = new CategoryDeleteDto();
        dto.setId(id);
        assertEquals(id, dto.getId());
    }

    @Test
    void equalsAndHashCode_shouldBeBasedOnId() {
        UUID id = UUID.randomUUID();
        CategoryDeleteDto dto1 = new CategoryDeleteDto(id);
        CategoryDeleteDto dto2 = new CategoryDeleteDto(id);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        UUID id = UUID.randomUUID();
        CategoryDeleteDto dto = new CategoryDeleteDto(id);
        String toString = dto.toString();

        assertTrue(toString.contains("id"));
        assertTrue(toString.contains(id.toString()));
    }

    @Test
    void validation_shouldFailWhenIdIsNull() {
        CategoryDeleteDto dto = new CategoryDeleteDto(null);
        Set<ConstraintViolation<CategoryDeleteDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals("Category ID must not be null",
                violations.iterator().next().getMessage());
    }

    @Test
    void validation_shouldPassWhenIdIsNotNull() {
        CategoryDeleteDto dto = new CategoryDeleteDto(UUID.randomUUID());
        Set<ConstraintViolation<CategoryDeleteDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}