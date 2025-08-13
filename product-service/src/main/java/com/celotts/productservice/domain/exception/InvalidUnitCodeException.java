package com.celotts.productservice.domain.exception;

public class InvalidUnitCodeException extends RuntimeException {
    public InvalidUnitCodeException(String code) {
        super("El código de unidad no es válido: " + code);
    }
}
