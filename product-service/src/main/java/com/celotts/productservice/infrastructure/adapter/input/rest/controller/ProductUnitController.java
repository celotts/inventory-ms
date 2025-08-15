package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.product.unit.usecase.ProductUnitUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-units")
public class ProductUnitController {

    // 👉 Depender del puerto (interfaz), no de la implementación
    private final ProductUnitUseCase productUnitUseCase;
    private final ProductUnitDtoMapper productUnitDtoMapper;

    @Operation(summary = "Crea una nueva unidad de producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostConstruct
    public void init() {
        log.info("✅ ProductUnitController instanciado.");
    }

    @PostMapping
    public ResponseEntity<ProductUnitResponseDto> create(@Valid @RequestBody ProductUnitCreateDto dto) {
        ProductUnitModel toSave = productUnitDtoMapper.toModel(dto);
        ProductUnitModel saved = productUnitUseCase.save(toSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(productUnitDtoMapper.toResponseDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<ProductUnitResponseDto>> findAll() {
        List<ProductUnitResponseDto> list = productUnitUseCase.findAll()
                .stream()
                .map(productUnitDtoMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductUnitResponseDto> findById(@PathVariable UUID id) {
        ProductUnitModel model = productUnitUseCase.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductUnit not found: " + id));
        return ResponseEntity.ok(productUnitDtoMapper.toResponseDto(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductUnitResponseDto> update(@PathVariable UUID id,
                                                         @Valid @RequestBody ProductUnitUpdateDto dto) {
        ProductUnitModel changes = productUnitDtoMapper.toModel(dto);
        ProductUnitModel updated = productUnitUseCase.update(id, changes);
        return ResponseEntity.ok(productUnitDtoMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productUnitUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists-by-code/{code}")
    public ResponseEntity<Map<String, Boolean>> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(Map.of("exists", productUnitUseCase.existsByCode(code)));
    }

    @GetMapping("/name-by-code/{code}")
    public ResponseEntity<String> findByName(@PathVariable String code) {
        Optional<String> name = productUnitUseCase.findNameByCode(code);
        return name.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/code")
    public ResponseEntity<List<String>> findAllByCode() {
        return ResponseEntity.ok(productUnitUseCase.findAllCodes());
    }
}
