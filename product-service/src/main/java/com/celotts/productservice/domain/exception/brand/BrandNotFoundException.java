package com.celotts.productservice.domain.exception.brand;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import java.util.UUID;

public class BrandNotFoundException extends ResourceNotFoundException {
    public BrandNotFoundException(UUID id) {
        super("brand.not-found", id);
    }
    public BrandNotFoundException(String message) {
        super(message);
    }
}