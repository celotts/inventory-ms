package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductUnitService;
import com.celotts.productservice.domain.port.product.root.input.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/product-unit")
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
public class ProductUnitController {

    private final ProductUnitService productUnitService;
    private final ProductUseCase productUnitUseCase;

    @Operation(summary = "Crea una nueva unidad de producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })

    @PostMapping
    public ResponseEntity<ProductUnitResponseDto> create(@Valid @RequestBody ProductUnitCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productUnitService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductUnitResponseDto>> findAll() {
        return ResponseEntity.ok(productUnitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductUnitResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productUnitService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductUnitResponseDto> update(@PathVariable UUID id, @Valid @RequestBody ProductUnitUpdateDto dto) {
        return ResponseEntity.ok(productUnitService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productUnitService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/exists-by-code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(productUnitService.existsByCode(code));
    }
    
    @GetMapping("/name-by-code/{code}")
    public ResponseEntity<String> findByName(@PathVariable String code) {
        Optional<String> name = productUnitService.findNameByCode(code);
        return name.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/code")
    public ResponseEntity<List<String>> findAllByCode() {
        return ResponseEntity.ok(productUnitService.findAllCodes());
    }

}
