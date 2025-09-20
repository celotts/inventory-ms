package com.celotts.productservice.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductIdExistsValidator.class)
public @interface ProductIdExists {
    String message() default "ProductId does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}