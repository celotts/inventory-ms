package com.celotts.productservice.domain.exception;

import java.util.UUID;

public class InvalidBrandIdException extends ResourceNotFoundException {
    public InvalidBrandIdException(UUID id) {
        super("Brand", id);
    }
}