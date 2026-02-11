package com.celotts.productservice.domain.exception.product;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;

import java.util.UUID;

public class ProductNotFoundException extends ResourceNotFoundException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(UUID id) {
        super("product.not-found.id", id);
    }
}
