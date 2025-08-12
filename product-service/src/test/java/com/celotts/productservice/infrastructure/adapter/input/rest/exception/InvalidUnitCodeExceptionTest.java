package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.InvalidUnitCodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidUnitCodeExceptionTest {

    @Test
    void testExceptionMessageWithCode() {
        String code = "UNIT01";

        InvalidUnitCodeException exception = new InvalidUnitCodeException(code);

        assertEquals("El código de unidad no es válido: UNIT01", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        InvalidUnitCodeException exception = new InvalidUnitCodeException("X");

        assertTrue(exception instanceof RuntimeException);
    }
}