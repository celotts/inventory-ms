package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productserviceOld.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BrandNotFoundExceptionTest {
    @Test
    void ctor_con_id() {
        UUID id = UUID.randomUUID();
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", id);

        assertThat(ex.getMessage())
                .containsIgnoringCase("product")
                .containsIgnoringCase("not found")
                .contains(id.toString());
    }
}