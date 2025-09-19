package com.celotts.productservice.infrastructure.validation;

import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductUnitJpaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnitCodeExistsValidator implements ConstraintValidator<UnitCodeExists, String> {
    private final ProductUnitJpaRepository productUnitJpaRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // usa @NotBlank en el DTO si quieres forzarlo
        return productUnitJpaRepository.existsByCodeIgnoreCase(value);
    }
}