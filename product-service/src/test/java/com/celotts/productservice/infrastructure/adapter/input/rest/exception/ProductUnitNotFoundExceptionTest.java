package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductUnitNotFoundExceptionTest {

    @Test
    void ctor_con_id() {
        UUID id = UUID.randomUUID();
        ResourceNotFoundException ex = new ResourceNotFoundException("ProductUnit", id);

        assertThat(ex.getMessage())
                .containsIgnoringCase("productunit")
                .containsIgnoringCase("not found")
                .contains(id.toString());
    }
}