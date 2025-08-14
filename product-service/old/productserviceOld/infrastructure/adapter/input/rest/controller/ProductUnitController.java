package com.celotts.productserviceOld.infrastructure.adapter.input.rest.controller;

import com.celotts.productserviceOld.applications.service.ProductUnitService;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitUpdateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
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

    private final ProductUnitService productUnitService;
    private final ProductUnitDtoMapper productUnitDtoMapper;

    @Operation(summary = "Crea una nueva unidad de producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })

    @PostConstruct
    public void init() {
        log.info("✅ ProductUnitController fue instanciado correctamente por Spring.");
    }

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
    public ResponseEntity<Map<String, Boolean>> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(Map.of("exists", productUnitService.existsByCode(code)));
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
