package com.celotts.productservice.domain.exception.product;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;

import java.util.UUID;

public class ProductTagAssignmentNotFoundException extends ResourceNotFoundException {

    public ProductTagAssignmentNotFoundException(UUID id) {
        super("product-tag-assignment.not-found.id", id);
    }
}