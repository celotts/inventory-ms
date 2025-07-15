package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import java.util.UUID;

public class ProductUnitNotFoundException extends RuntimeException {

    public ProductUnitNotFoundException(UUID id) {
        super("Could not find product unit with id " + id);
    }

    public ProductUnitNotFoundException(String message) {
        super(message);
    }
}
