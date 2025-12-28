package com.celotts.purchaseservice.infrastructure.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimmedNotBlankValidator implements ConstraintValidator<TrimmedNotBlank, String> {
    private int min;

    @Override
    public void initialize(TrimmedNotBlank annotation) {
        this.min = annotation.min();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        String trimmed = value.trim();
        return !trimmed.isEmpty() && trimmed.length() >= min;
    }
}