package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import java.util.UUID;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(UUID id) {
        super("Brand not found with ID: " + id);
    }
    //TODO: No se usa
    public BrandNotFoundException(String message) {
        super(message);
    }
}
