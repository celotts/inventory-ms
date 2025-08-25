package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.port.input.product.ProductUnitUseCase;
import com.celotts.productservice.domain.model.product.ProductUnitModel; // <-- si tu modelo está en .domain.exception.model como mostró tu grep
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit.ProductUnitUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productunit.ProductUnitDtoMapper;
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
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-units")
public class ProductUnitController {

    private final ProductUnitUseCase productUnitUseCase;     // <-- inyectado
    private final ProductUnitDtoMapper productUnitDtoMapper; // <-- inyectado

    @PostConstruct
    public void init() {
        log.info("✅ ProductUnitController fue instanciado correctamente por Spring.");
    }

    @Operation(summary = "Crea una nueva unidad de producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ProductUnitResponseDto> create(@Valid @RequestBody ProductUnitCreateDto dto) {
        ProductUnitModel model = productUnitDtoMapper.toModel(dto);            // DTO -> Model
        ProductUnitModel saved = productUnitUseCase.save(model);               // usar el CAMPO, no la clase
        ProductUnitResponseDto resp = productUnitDtoMapper.toResponseDto(saved); // Model -> DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<ProductUnitResponseDto>> findAll() {
        List<ProductUnitResponseDto> list = productUnitDtoMapper
                .toResponseDtoList(productUnitUseCase.findAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductUnitResponseDto> findById(@PathVariable UUID id) {
        return productUnitUseCase.findById(id)
                .map(productUnitDtoMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Si NO tienes update en el UseCase, puedes reutilizar save() tras mergear con el mapper
    @PutMapping("/{id}")
    public ResponseEntity<ProductUnitResponseDto> update(@PathVariable UUID id,
                                                         @Valid @RequestBody ProductUnitUpdateDto dto) {
        return productUnitUseCase.findById(id)
                .map(existing -> {
                    // Aplica cambios EN SITIO (void)
                    productUnitDtoMapper.apply(existing, dto);

                    // Persiste y mapea la respuesta
                    ProductUnitModel saved = productUnitUseCase.save(existing);
                    return ResponseEntity.ok(productUnitDtoMapper.toResponseDto(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        // agrega productUnitUseCase.delete(id) si existe en el puerto
        // productUnitUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}