package com.celotts.productservice.infrastructure.adapter.input.rest.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestDummyController {

    @GetMapping("/product-not-found/{id}")
    public void throwProductNotFound(@PathVariable UUID id) {
        throw new ProductNotFoundException(id);
    }

    @GetMapping("/product-already-exists")
    public void throwProductAlreadyExists() {
        throw new ProductAlreadyExistsException("Ya existe un producto con el código: P-001");
    }

    @GetMapping("/invalid-product-type")
    public void throwInvalidProductType() {
        throw new InvalidProductTypeCodeException("El código de tipo de producto no es válido: TP-001");
    }

    @GetMapping("/invalid-unit-code")
    public void throwInvalidUnitCode() {
        throw new InvalidUnitCodeException("El código de unidad no es válido: UN-001");
    }

    @GetMapping("/invalid-brand-id/{id}")
    public void throwInvalidBrandId(@PathVariable UUID id) {
        throw new InvalidBrandIdException(id);
    }

    @GetMapping("/illegal-argument")
    public void throwIllegalArgument() {
        throw new IllegalArgumentException("Este argumento no es válido");
    }

    @GetMapping("/data-integrity-violation")
    public void throwDataIntegrityViolation() {
        throw new DataIntegrityViolationException("violates foreign key constraint \"fk_product_brand\"");
    }

    @GetMapping("/global-exception")
    public void throwGlobalException() {
        throw new RuntimeException("Fallo inesperado");
    }
}