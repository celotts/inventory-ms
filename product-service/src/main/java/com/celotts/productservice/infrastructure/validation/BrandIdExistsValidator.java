package com.celotts.productservice.infrastructure.validation;

import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductBrandJpaRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BrandIdExistsValidator implements ConstraintValidator<BrandIdExists, UUID> {

    private final ProductBrandJpaRepository productBrandJpaRepository;

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        if (value == null) return true; // si quieres que sea obligatorio, combínalo con @NotNull en el DTO
        return productBrandJpaRepository.existsById(value);
    }
}