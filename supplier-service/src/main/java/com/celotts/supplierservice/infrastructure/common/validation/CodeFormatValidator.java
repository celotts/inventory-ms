package com.celotts.supplierservice.infrastructure.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CodeFormatValidator implements ConstraintValidator<CodeFormat, String> {
    private static final String REGEX = "^[A-Z0-9\\-_]{3,40}$";
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) return true; // usa @NotNull si lo necesitas obligatorio
        return value.matches(REGEX);
    }
}
