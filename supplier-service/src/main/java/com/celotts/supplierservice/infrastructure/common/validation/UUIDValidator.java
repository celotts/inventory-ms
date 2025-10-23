package com.celotts.supplierservice.infrastructure.common.validation;

import jakarta.validation.*;
import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<UUIDv, String> {
    public boolean isValid(String v, ConstraintValidatorContext c){
        if (v == null) return false;
        try { UUID.fromString(v); return true; } catch (Exception e) { return false; }
    }
}