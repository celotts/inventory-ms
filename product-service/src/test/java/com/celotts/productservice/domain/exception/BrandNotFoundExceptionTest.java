package com.celotts.productservice.domain.exception;

import com.celotts.productservice.domain.exception.BrandNotFoundException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BrandNotFoundExceptionTest {

    @Test
    void ctor_withUuid_buildsMessageAndKeepsHierarchy() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        BrandNotFoundException ex = new BrandNotFoundException(id);

        // Assert
        assertThat(ex)
                .isInstanceOf(ResourceNotFoundException.class)
                .isInstanceOf(RuntimeException.class);
        assertThat(ex.getMessage())
                .isEqualTo("Brand not found with id: " + id);
    }

    @Test
    void ctor_withCustomMessage_usesGivenMessage() {
        // Arrange
        String message = "Custom brand not found message";

        // Act
        BrandNotFoundException ex = new BrandNotFoundException(message);

        // Assert
        assertThat(ex)
                .isInstanceOf(ResourceNotFoundException.class)
                .isInstanceOf(RuntimeException.class);
        assertThat(ex.getMessage()).isEqualTo(message);
    }
}