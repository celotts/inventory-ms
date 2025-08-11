package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitNotFoundExceptionTest {

    @Test
    void testConstructorWithUUID() {
        UUID id = UUID.randomUUID();
        ProductUnitNotFoundException exception = new ProductUnitNotFoundException(id);

        assertEquals("Could not find product unit with id " + id, exception.getMessage());
    }

    @Test
    void testConstructorWithCustomMessage() {
        String message = "No se encontró la unidad de producto con código U001";
        ProductUnitNotFoundException exception = new ProductUnitNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testIsRuntimeException() {
        ProductUnitNotFoundException exception = new ProductUnitNotFoundException("test");
        assertTrue(exception instanceof RuntimeException);
    }
}