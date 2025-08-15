package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceAlreadyExistsExceptionTest {

    @Test
    void buildsMessage() {
        String message = "dummy";
        ResourceAlreadyExistsException ex =
                new ResourceAlreadyExistsException("Product", message);

        assertThat(ex.getMessage())
                .contains("Product")
                .contains(message);
    }
}