package com.celotts.taxservice.infrastructure.common.validation;

import com.celotts.taxservice.infrastructure.adapter.output.postgres.repository.tax.TaxJpaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaxIdExistsValidator implements ConstraintValidator<TaxIdExists, UUID> {

    private final TaxJpaRepository taxJpaRepository;

    public TaxIdExistsValidator(TaxJpaRepository taxJpaRepository) {
        this.taxJpaRepository = taxJpaRepository;
    }

    @Override
    public void initialize(TaxIdExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        // Si el valor es nulo, retorna true (permitir nulos si es opcional)
        if (value == null) {
            return true;
        }

        // Verificar si existe el impuesto con el ID proporcionado
        return taxJpaRepository.existsById(value);
    }
}
