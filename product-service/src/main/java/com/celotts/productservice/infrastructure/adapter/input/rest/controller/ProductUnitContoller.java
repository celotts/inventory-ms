package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductUnitService;
import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.product.root.input.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/product-unit")
@CrossOrigin(origins = "*")
public class ProductUnitContoller {

    private final ProductUnitService productUnitService;
    private final ProductUseCase productUnitUseCase;

    @PostMapping
    public ResponseEntity<ProductUnitResponseDto> create(@Valid @RequestBody ProductUnitCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productUnitService.create(dto));
    }
}
