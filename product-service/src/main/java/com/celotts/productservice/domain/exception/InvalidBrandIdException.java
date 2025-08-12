package com.celotts.productservice.domain.exception;

import java.util.UUID;

public class InvalidBrandIdException extends RuntimeException {
    public InvalidBrandIdException(UUID id) {
        super("El ID de marca no es válido: " + id);
    }
}
