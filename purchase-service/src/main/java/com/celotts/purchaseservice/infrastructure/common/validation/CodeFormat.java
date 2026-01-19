package com.celotts.purchaseservice.infrastructure.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CodeFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeFormat {

    String message() default "{validation.code-format.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}