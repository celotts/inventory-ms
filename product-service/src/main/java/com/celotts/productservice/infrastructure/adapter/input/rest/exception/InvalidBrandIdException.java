package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import java.util.UUID;

public class InvalidBrandIdException extends RuntimeException {
    public InvalidBrandIdException(UUID id) {
        super("El ID de marca no es válido: " + id);
    }
}
