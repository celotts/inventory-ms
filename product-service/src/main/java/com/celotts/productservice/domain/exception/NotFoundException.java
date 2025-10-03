// domain/exception/NotFoundException.java
package com.celotts.productservice.domain.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}