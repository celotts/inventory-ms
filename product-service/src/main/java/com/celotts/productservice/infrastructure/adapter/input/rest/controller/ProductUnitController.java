package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductUnitModel;
import com.celotts.productservice.domain.port.input.product.ProductUnitUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit.ProductUnitUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.ListResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productunit.ProductUnitMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-units")
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Slf4j
@Tag(name = "${swagger.product-unit.api.name}", description = "${swagger.product-unit.api.desc}")
public class ProductUnitController {

    private final ProductUnitUseCase productUnitUseCase;
    private final ProductUnitMapper mapper;

    @Operation(summary = "${swagger.product-unit.create.summary}", description = "${swagger.product-unit.create.desc}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Drive created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })

    @PostMapping
    public ResponseEntity<ProductUnitResponseDto> create(@Valid @RequestBody ProductUnitCreateDto dto) {
        ProductUnitModel toCreate = mapper.toModel(dto);
        ProductUnitModel created = productUnitUseCase.create(toCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-units/" + created.getId())
                .body(mapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "${swagger.product-unit.list.summary}")
    public ResponseEntity<ListResponse<ProductUnitResponseDto>> findAll() {
        var models = productUnitUseCase.findAll();
        return ResponseEntity.ok(ListResponse.of(mapper.toResponseList(models)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "${swagger.product-unit.get-by-id.summary}")
    public ResponseEntity<ProductUnitResponseDto> findById(@PathVariable UUID id) {
        return productUnitUseCase.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "${swagger.product-unit.update.summary}")
    public ResponseEntity<ProductUnitResponseDto> update(@PathVariable UUID id,
                                                         @Valid @RequestBody ProductUnitUpdateDto dto) {
        // Patch parcial con MapStruct (ignora nulls)
        return productUnitUseCase.findById(id)
                .map(existing -> {
                    mapper.updateModelFromDto(existing, dto);
                    ProductUnitModel saved = productUnitUseCase.update(id, existing);
                    return ResponseEntity.ok(mapper.toResponse(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "${swagger.product-unit.delete.summary}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productUnitUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
