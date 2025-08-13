package com.celotts.productservice.domain.exception;

import com.celotts.productservice.infrastructure.common.error.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainExceptionTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {
        // Arrange
        ErrorCode code = ErrorCode.VALIDATION_ERROR;
        int httpStatus = 400;
        String message = "Validation failed for field X";

        // Act
        DomainException ex = new DomainException(code, httpStatus, message);

        // Assert
        assertThat(ex.getCode()).isEqualTo(code);
        assertThat(ex.getHttpStatus()).isEqualTo(httpStatus);
        assertThat(ex.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldInheritFromRuntimeException() {
        // Arrange & Act
        DomainException ex = new DomainException(ErrorCode.INTERNAL_ERROR, 500, "Something went wrong");

        // Assert
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }
}