package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BrandNotFoundExceptionTest {

    @Test
    void testConstructorWithUUID() {
        UUID id = UUID.randomUUID();
        BrandNotFoundException exception = new BrandNotFoundException(id);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Brand not found with ID: " + id);
    }

    @Test
    void testConstructorWithCustomMessage() {
        String message = "Custom brand not found message";
        BrandNotFoundException exception = new BrandNotFoundException(message);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}