package com.celotts.productservice.domain.exception;

import java.util.UUID;

public class BrandNotFoundException extends ResourceNotFoundException {
    public BrandNotFoundException(UUID id) { super("Brand", id); }
    public BrandNotFoundException(String message) { super(message); }
}