package com.celotts.supplierservice.infrastructure.common.validation;

import jakarta.validation.*;
import org.hibernate.validator.internal.constraintvalidators.hv.UUIDValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UUIDValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UUIDv {
    String message() default "invalid UUID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}