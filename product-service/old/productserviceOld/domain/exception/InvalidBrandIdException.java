package com.celotts.productserviceOld.domain.exception;

import java.util.UUID;

public class InvalidBrandIdException extends RuntimeException {
    public InvalidBrandIdException(UUID id) {
        super("El ID de marca no es válido: " + id);
    }
}
