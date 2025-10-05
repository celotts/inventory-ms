package com.celotts.productservice.domain.exception.brand;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;

import java.util.UUID;

public class InvalidBrandIdException extends ResourceNotFoundException {
    public InvalidBrandIdException(UUID id) {
        super("Brand", id);
    }
}