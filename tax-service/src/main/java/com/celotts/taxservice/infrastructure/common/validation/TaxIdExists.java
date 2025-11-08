package com.celotts.taxservice.infrastructure.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TaxIdExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TaxIdExists {

    String message() default "El impuesto con el ID especificado no existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}