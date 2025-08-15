package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-brands")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
public class ProductBrandController {

    // ⬇️ Cambia Service → UseCase (puerto)
    private final ProductBrandUseCase productBrandUseCase;
    private final ProductBrandDtoMapper mapper;

    @PostMapping
    public ResponseEntity<ProductBrandResponseDto> create(@Valid @RequestBody ProductBrandCreateDto dto) {
        // (Reglas simples aquí; la validación fuerte vive en el caso de uso/repositorio)
        ProductBrandModel model = mapper.toModel(dto);
        model.setCreatedAt(LocalDateTime.now());
        model.setCreatedBy(dto.getCreatedBy());

        ProductBrandModel saved = productBrandUseCase.save(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponseDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<ProductBrandResponseDto>> findAll() {
        List<ProductBrandResponseDto> list = productBrandUseCase.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> findById(@PathVariable UUID id) {
        return productBrandUseCase.findById(id)
                .map(mapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBrandResponseDto> update(@PathVariable UUID id,
                                                          @Valid @RequestBody ProductBrandUpdateDto dto) {
        ProductBrandModel existing = productBrandUseCase.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductBrand not found with id: " + id));

        // Verificación simple de nombre duplicado
        productBrandUseCase.findByName(dto.getName())
                .filter(b -> !b.getId().equals(id))
                .ifPresent(b -> { throw new IllegalArgumentException("ProductBrand with name '" + dto.getName() + "' already exists"); });

        // Actualizar campos
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setEnabled(dto.getEnabled());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        ProductBrandModel updated = productBrandUseCase.save(existing);
        return ResponseEntity.ok(mapper.toResponseDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!productBrandUseCase.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productBrandUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<ProductBrandResponseDto> findByName(@PathVariable String name) {
        return productBrandUseCase.findByName(name)
                .map(mapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/exists-by-name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        return ResponseEntity.ok(productBrandUseCase.existsByName(name));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductBrandResponseDto> enable(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponseDto(productBrandUseCase.enableBrand(id)));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductBrandResponseDto> disable(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponseDto(productBrandUseCase.disableBrand(id)));
    }
}