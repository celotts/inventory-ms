package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productserviceOld.domain.exception.InvalidProductTypeCodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidProductTypeCodeExceptionTest {

    @Test
    void testExceptionMessageWithCode() {
        String code = "FOOD123";

        InvalidProductTypeCodeException exception = new InvalidProductTypeCodeException(code);

        assertEquals("El código de tipo de producto no es válido: FOOD123", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        InvalidProductTypeCodeException exception = new InvalidProductTypeCodeException("X");

        assertTrue(exception instanceof RuntimeException);
    }
}