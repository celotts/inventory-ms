package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.domain.port.input.product.ProductTypeUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.producttype.ProductTypeMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-type")
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Slf4j
public class ProductTypeController {
    private final ProductTypeUseCase productTypeUseCase;
    private final ProductTypeMapper mapper;

    @PostMapping
    public ResponseEntity<ProductTypeResponseDto> create(@Valid @RequestBody ProductTypeCreateDto dto) {
        ProductTypeModel toCreate = mapper.toModel(dto);
        ProductTypeModel created = productTypeUseCase.create(toCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-types/" + created.getId())
                .body(mapper.toResponse(created));
    }
}