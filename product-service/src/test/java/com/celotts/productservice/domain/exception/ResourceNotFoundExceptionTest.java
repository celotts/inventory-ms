package com.celotts.productservice.domain.exception;

import com.celotts.productserviceOld.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_withResourceAndId_shouldGenerateExpectedMessage() {
        // Arrange
        String resource = "Product";
        UUID id = UUID.randomUUID();

        // Act
        ResourceNotFoundException ex = new ResourceNotFoundException(resource, id);

        // Assert
        assertThat(ex)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(resource + " not found with id: " + id);
    }

    @Test
    void constructor_withMessage_shouldUseProvidedMessage() {
        // Arrange
        String message = "Custom not found message";

        // Act
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        // Assert
        assertThat(ex)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(message);
    }
}