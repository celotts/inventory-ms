package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.InvalidBrandIdException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InvalidBrandIdExceptionTest {

    @Test
    void testExceptionMessageWithUUID() {
        UUID brandId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        InvalidBrandIdException exception = new InvalidBrandIdException(brandId);

        assertEquals("El ID de marca no es válido: 123e4567-e89b-12d3-a456-426614174000", exception.getMessage());
    }

    @Test
    void testExceptionIsInstanceOfRuntimeException() {
        UUID brandId = UUID.randomUUID();

        InvalidBrandIdException exception = new InvalidBrandIdException(brandId);

        assertTrue(exception instanceof RuntimeException);
    }
}