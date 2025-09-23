package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productcategory.ProductCategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productcategory.ProductCategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productcategory.ProductCategoryMapper;
import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.domain.port.input.product.ProductCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Category API", description = "API for managing product categories")
public class ProductCategoryController {

    private final ProductCategoryUseCase productCategoryUseCase;
    private final ProductCategoryMapper productCategoryMapper;

    @PostMapping
    @Operation(summary = "Assign category to product")
    public ResponseEntity<ProductCategoryResponseDto> create(@Valid @RequestBody ProductCategoryCreateDto dto) {
        ProductCategoryModel modelIn = productCategoryMapper.toModel(dto);                // ✅ MapStruct
        ProductCategoryModel saved   = productCategoryUseCase.assignCategoryToProduct(modelIn);
        ProductCategoryResponseDto out = productCategoryMapper.toResponse(saved);         // ✅ MapStruct
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assignment by ID")
    public ResponseEntity<ProductCategoryResponseDto> getById(@PathVariable UUID id) {
        ProductCategoryModel model = productCategoryUseCase.getById(id);
        return ResponseEntity.ok(productCategoryMapper.toResponse(model));                // ✅ MapStruct
    }

    @GetMapping
    @Operation(summary = "List all assignments")
    public ResponseEntity<List<ProductCategoryResponseDto>> getAll() {
        List<ProductCategoryModel> all = productCategoryUseCase.getAll();
        return ResponseEntity.ok(productCategoryMapper.toResponseList(all));              // ✅ sin Collectors
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Disable assignment (logical)")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        productCategoryUseCase.disableById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @Operation(summary = "Delete assignment (physical)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productCategoryUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}