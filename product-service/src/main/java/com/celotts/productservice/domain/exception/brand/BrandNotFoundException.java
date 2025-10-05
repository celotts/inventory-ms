package com.celotts.productservice.domain.exception.brand;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;

import java.util.UUID;

public class BrandNotFoundException extends ResourceNotFoundException {
    public BrandNotFoundException(UUID id) { super("Brand", id); }
    public BrandNotFoundException(String message) { super(message); }
}