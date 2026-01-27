package com.celotts.purchaseservice.infrastructure.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<UUIDv, String> {
    public boolean isValid(String v, ConstraintValidatorContext c){
        if (v == null) return false;
        try { UUID.fromString(v); return true; } catch (Exception e) { return false; }
    }
}