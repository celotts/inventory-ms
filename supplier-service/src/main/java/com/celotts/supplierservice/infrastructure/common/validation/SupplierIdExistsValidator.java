package com.celotts.supplierservice.infrastructure.common.validation;

import com.celotts.supplierservice.infrastructure.adapter.output.postgres.repository.supplier.SupplierJpaRepository;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SupplierIdExistsValidator implements ConstraintValidator<SupplierIdExists, UUID> {
    private final SupplierJpaRepository repo;
    public boolean isValid(UUID id, ConstraintValidatorContext c) {
        return id != null && repo.existsById(id);
    }
}