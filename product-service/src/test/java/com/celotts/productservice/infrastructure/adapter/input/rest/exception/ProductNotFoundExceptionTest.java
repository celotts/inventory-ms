package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Product not found with code: CODE123";
        ProductNotFoundException exception = new ProductNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithUUID() {
        UUID id = UUID.randomUUID();
        ProductNotFoundException exception = new ProductNotFoundException(id);

        assertEquals("Product not found with ID: " + id, exception.getMessage());
    }

    @Test
    void testIsRuntimeException() {
        ProductNotFoundException exception = new ProductNotFoundException("some message");
        assertTrue(exception instanceof RuntimeException);
    }
}