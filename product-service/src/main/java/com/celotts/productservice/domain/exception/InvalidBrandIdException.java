package com.celotts.productservice.domain.exception;

import java.util.UUID;

public class InvalidBrandIdException extends RuntimeException {
    public InvalidBrandIdException(UUID id) {
        super("The brand ID is not valid: " + id);
    }
}
