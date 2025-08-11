package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductAlreadyExistsExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Ya existe un producto con el código: P001";
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionInheritance() {
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException("dummy");
        assertTrue(exception instanceof RuntimeException);
    }
}