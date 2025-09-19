package com.celotts.productservice.infrastructure.validation;

import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductJpaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductIdExistsValidator implements ConstraintValidator<ProductIdExists, UUID> {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        if (value == null) return true; // controla @NotNull en el DTO
        return productJpaRepository.existsById(value);
    }
}