package com.celotts.productservice.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryIdExistsValidator.class)
public @interface CategoryIdExists {

    String message() default "CategoryId does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}