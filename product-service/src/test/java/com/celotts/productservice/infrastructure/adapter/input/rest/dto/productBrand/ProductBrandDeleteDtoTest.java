package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandDeleteDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandDeleteDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDto_shouldHaveNoViolations() {
        ProductBrandDeleteDto dto = new ProductBrandDeleteDto(UUID.randomUUID());

        Set<ConstraintViolation<ProductBrandDeleteDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void testNullId_shouldTriggerValidationError() {
        ProductBrandDeleteDto dto = new ProductBrandDeleteDto(null);

        Set<ConstraintViolation<ProductBrandDeleteDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Expected 1 validation violation for null id");
        assertEquals("Category ID is required", violations.iterator().next().getMessage());
    }

    @Test
    void testGettersAndSetters_shouldWorkCorrectly() {
        UUID id = UUID.randomUUID();
        ProductBrandDeleteDto dto = new ProductBrandDeleteDto();
        dto.setId(id);

        assertEquals(id, dto.getId());
    }

    @Test
    void testEqualsAndHashCode_shouldBeEqual() {
        UUID id = UUID.randomUUID();

        ProductBrandDeleteDto dto1 = new ProductBrandDeleteDto(id);
        ProductBrandDeleteDto dto2 = new ProductBrandDeleteDto(id);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString_shouldContainId() {
        UUID id = UUID.randomUUID();
        ProductBrandDeleteDto dto = new ProductBrandDeleteDto(id);

        String str = dto.toString();
        assertTrue(str.contains(id.toString()));
    }
}