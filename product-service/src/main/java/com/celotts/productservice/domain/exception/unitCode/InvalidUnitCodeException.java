package com.celotts.productservice.domain.exception.unitCode;

public class InvalidUnitCodeException extends RuntimeException {
    public InvalidUnitCodeException(String code) {
        super("The unit code is not valid: " + code);
    }
}
