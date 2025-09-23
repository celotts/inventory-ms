package com.celotts.productservice.domain.exception;

public class InvalidUnitCodeException extends RuntimeException {
    public InvalidUnitCodeException(String code) {
        super("The unit code is not valid: " + code);
    }
}
