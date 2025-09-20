package com.celotts.productservice.infrastructure.validation;

import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryJpaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryIdExistsValidator implements ConstraintValidator<CategoryIdExists, UUID> {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // si quieres que null se valide aparte con @NotNull
        }
        return categoryJpaRepository.existsById(value);
    }
}