package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryCreateDtoTest {

    private Validator newValidator() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        return f.getValidator();
    }

    @Test
    void builder_buildsAllFields_andGettersWork() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime assignedAt = LocalDateTime.now();

        ProductCategoryCreateDto dto = ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(assignedAt)
                .enabled(true)
                .createdBy("tester")
                .updatedBy("upd")
                .build();

        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getCategoryId()).isEqualTo(categoryId);
        assertThat(dto.getAssignedAt()).isEqualTo(assignedAt);
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getCreatedBy()).isEqualTo("tester");
        assertThat(dto.getUpdatedBy()).isEqualTo("upd");
    }

    @Test
    void settersAndEqualsHashCode_andToStringContainValues() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime assignedAt = LocalDateTime.now();

        ProductCategoryCreateDto a = ProductCategoryCreateDto.builder().build();
        a.setProductId(productId);
        a.setCategoryId(categoryId);
        a.setAssignedAt(assignedAt);
        a.setEnabled(false);
        a.setCreatedBy("creator");
        a.setUpdatedBy("upd");

        ProductCategoryCreateDto b = ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(assignedAt)
                .enabled(false)
                .createdBy("creator")
                .updatedBy("upd")
                .build();

        // equals/hashCode (iguales)
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        // equals: misma instancia
        assertThat(a).isEqualTo(a);
        // equals: contra null y otra clase
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("no-dto");

        // equals: distinto en un campo relevante
        ProductCategoryCreateDto c = ProductCategoryCreateDto.builder()
                .productId(UUID.randomUUID()) // distinto
                .categoryId(categoryId)
                .assignedAt(assignedAt)
                .enabled(false)
                .createdBy("creator")
                .updatedBy("upd")
                .build();
        assertThat(a).isNotEqualTo(c);

        String ts = a.toString();
        assertThat(ts).contains("creator").contains("upd");
    }

    @Test
    void validation_failsWhenRequiredFieldsMissing() {
        Validator validator = newValidator();
        ProductCategoryCreateDto empty = ProductCategoryCreateDto.builder().build();

        Set<ConstraintViolation<ProductCategoryCreateDto>> violations = validator.validate(empty);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(3); // productId, categoryId, enabled, createdBy

        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("productId"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("categoryId"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("enabled"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdBy"))).isTrue();
    }

    @Test
    void validation_passesWhenFieldsAreValid() {
        Validator validator = newValidator();
        ProductCategoryCreateDto valid = ProductCategoryCreateDto.builder()
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .enabled(Boolean.TRUE)
                .createdBy("creator")
                .build();

        Set<ConstraintViolation<ProductCategoryCreateDto>> violations = validator.validate(valid);
        assertThat(violations).isEmpty();
    }
}