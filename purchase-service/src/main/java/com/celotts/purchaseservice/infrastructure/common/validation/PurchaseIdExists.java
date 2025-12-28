package com.celotts.purchaseservice.infrastructure.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PurchaseIdExistsValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PurchaseIdExists {

    // Clave internacionalizada (ya no texto duro)
    String message() default "{validation.supplier.id.not-found}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}