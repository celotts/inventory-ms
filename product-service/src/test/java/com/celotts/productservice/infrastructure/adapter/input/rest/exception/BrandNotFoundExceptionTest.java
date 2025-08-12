package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.BrandNotFoundException; // <-- nuevo import

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BrandNotFoundExceptionTest {

    @Test
    void testConstructorWithUUID() {
        UUID id = UUID.randomUUID();
        BrandNotFoundException exception = new BrandNotFoundException(id);

        assertThat(exception).isNotNull();
        // No dependas del texto exacto: verifica que incluya la entidad y el id
        assertThat(exception.getMessage())
                .contains("Brand")
                .contains(id.toString());
    }

    @Test
    void testConstructorWithCustomMessage() {
        String message = "Custom brand not found message";
        BrandNotFoundException exception = new BrandNotFoundException(message);

        assertThat(exception).isNotNull();
        // El mensaje final puede ser formateado por ResourceNotFoundException;
        // validamos que incluya tu mensaje personalizado.
        assertThat(exception.getMessage()).contains(message);
    }
}