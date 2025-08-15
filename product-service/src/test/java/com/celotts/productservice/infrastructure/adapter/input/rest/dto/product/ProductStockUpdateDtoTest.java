package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductStockUpdateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductStockUpdateDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void validDto_shouldHaveNoViolations() {
        ProductStockUpdateDto dto = new ProductStockUpdateDto();
        dto.setStock(10);
        dto.setUpdatedBy("tester");

        Set<ConstraintViolation<ProductStockUpdateDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();

        // Lombok getters
        assertThat(dto.getStock()).isEqualTo(10);
        assertThat(dto.getUpdatedBy()).isEqualTo("tester");
    }

    @Test
    void nullStock_shouldFailValidation() {
        ProductStockUpdateDto dto = new ProductStockUpdateDto();
        dto.setStock(null);
        dto.setUpdatedBy("tester");

        Set<ConstraintViolation<ProductStockUpdateDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        ConstraintViolation<ProductStockUpdateDto> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString()).isEqualTo("stock");
        assertThat(v.getMessage()).contains("Stock value is required");
    }

    @Test
    void negativeStock_shouldFailValidation() {
        ProductStockUpdateDto dto = new ProductStockUpdateDto();
        dto.setStock(-5);
        dto.setUpdatedBy("tester");

        Set<ConstraintViolation<ProductStockUpdateDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        ConstraintViolation<ProductStockUpdateDto> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString()).isEqualTo("stock");
        assertThat(v.getMessage()).contains("greater than or equal to 0");
    }

    @Test
    void equalsAndHashCode_shouldWorkForSameValues() {
        ProductStockUpdateDto dto1 = new ProductStockUpdateDto();
        dto1.setStock(10);
        dto1.setUpdatedBy("tester");

        ProductStockUpdateDto dto2 = new ProductStockUpdateDto();
        dto2.setStock(10);
        dto2.setUpdatedBy("tester");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndFields() {
        ProductStockUpdateDto dto = new ProductStockUpdateDto();
        dto.setStock(5);
        dto.setUpdatedBy("tester");

        String s = dto.toString();
        assertThat(s).contains("ProductStockUpdateDto");
        assertThat(s).contains("stock=5");
        assertThat(s).contains("updatedBy=tester");
    }
}