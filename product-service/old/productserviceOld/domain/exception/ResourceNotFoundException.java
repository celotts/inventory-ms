package com.celotts.productserviceOld.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    // cuando quieras construir "Product not found with id: <id>"
    public ResourceNotFoundException(String resource, Object idOrKey) {
        super(resource + " not found with id: " + idOrKey);
    }

    // cuando ya tengas el mensaje listo
    public ResourceNotFoundException(String message) {
        super(message);
    }
}