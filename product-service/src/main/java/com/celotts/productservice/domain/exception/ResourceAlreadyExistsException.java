package com.celotts.productservice.domain.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resource, Object idOrKey) {
        super(resource + " already exists: " + idOrKey);
    }
}