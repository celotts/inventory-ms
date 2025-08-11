package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.infrastructure.adapter.input.rest.exception.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Profile("exception-test")
@RestController
@RequestMapping("/test")
public class ExceptionTestController {

    @GetMapping("/product-not-found/{id}")
    public void throwProductNotFound(@PathVariable UUID id) {
        throw new ProductNotFoundException(id);
    }

    @GetMapping("/product-already-exists")
    public void throwProductAlreadyExists() {
        throw new ProductAlreadyExistsException("Ya existe un producto con el código");
    }

    @GetMapping("/invalid-brand-id/{id}")
    public void throwInvalidBrandId(@PathVariable UUID id) {
        throw new InvalidBrandIdException(id);
    }

    @GetMapping("/illegal-argument")
    public void throwIllegalArgument() {
        throw new IllegalArgumentException("Este argumento no es válido");
    }

    @GetMapping("/global-exception")
    public void throwGlobalException() {
        throw new RuntimeException("Fallo inesperado");
    }
}