package com.celotts.purchaseservice.infrastructure.common.validation;

import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.repository.PurchaseJpaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PurchaseIdExistsValidator implements ConstraintValidator<PurchaseIdExists, UUID> {
    private final PurchaseJpaRepository repo;
    public boolean isValid(UUID id, ConstraintValidatorContext c) {
        return id != null && repo.existsById(id);
    }
}