package com.celotts.supplierservice.infrastructure.common.validation;

import jakarta.validation.*;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CodeFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeFormat {

    // Clave del mensaje internacionalizado
    String message() default "{validation.code-format.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}