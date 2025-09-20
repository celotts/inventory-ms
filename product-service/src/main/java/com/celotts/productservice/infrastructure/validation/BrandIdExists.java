package com.celotts.productservice.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BrandIdExistsValidator.class)
public @interface BrandIdExists {
    String message() default "BrandId does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
