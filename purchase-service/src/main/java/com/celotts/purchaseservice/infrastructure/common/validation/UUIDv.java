package com.celotts.purchaseservice.infrastructure.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
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