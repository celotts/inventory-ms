package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductResponseDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.ProductResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepositoryPort productRepositoryPort;

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productRepositoryPort.findAll()
                .stream()
                .map(ProductResponseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/id")
    public ProductResponseDTO getProductById(@RequestParam("id") UUID id) {
        return productRepositoryPort.findById(id)
                .map(ProductResponseMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}