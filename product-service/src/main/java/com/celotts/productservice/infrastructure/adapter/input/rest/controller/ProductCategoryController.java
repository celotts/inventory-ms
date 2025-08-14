package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.domain.port.input.product.ProductCategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productCategory.ProductCategoryDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product-category")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Tag(name = "Product Category API", description = "API para gestionar la relación entre productos y categorías")
public class ProductCategoryController {

    private final ProductCategoryUseCase productCategoryUseCase;
    private final ProductCategoryDtoMapper productCategoryDtoMapper;

    @PostMapping
    @Operation(summary = "Asignar categoría a producto")
    public ResponseEntity<ProductCategoryResponseDto> create(@RequestBody @Valid ProductCategoryCreateDto dto) {
        ProductCategoryModel created = productCategoryUseCase.assignCategoryToProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCategoryDtoMapper.toDto(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asignación por ID")
    public ResponseEntity<ProductCategoryResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(productCategoryDtoMapper.toDto(productCategoryUseCase.getById(id)));
    }

    @GetMapping
    @Operation(summary = "Listar todas las asignaciones")
    public ResponseEntity<List<ProductCategoryResponseDto>> getAll() {
        List<ProductCategoryResponseDto> response = productCategoryUseCase.getAll()
                .stream().map(productCategoryDtoMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar asignación (lógico)")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        productCategoryUseCase.disableById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @Operation(summary = "Eliminar asignación (físico)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productCategoryUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}