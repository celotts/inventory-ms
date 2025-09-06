package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.domain.port.input.product.ProductTagAssignmentUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment.ProductTagAssignmentResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTagAssignment.ProductTagAssignmentMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-tag-assignment")
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
public class ProductTagAssignmentController {

    private final ProductTagAssignmentUseCase useCase;
    private final ProductTagAssignmentMapper mapper;


    @PostMapping
    public ResponseEntity<ProductTagAssignmentResponseDto> create(
            @Valid @RequestBody ProductTagAssignmentCreateDto dto) {

        ProductTagAssignmentModel toCreate = mapper.toModel(dto);
        ProductTagAssignmentModel created  = useCase.create(toCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-tag-assignment/" + created.getId())
                .body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTagAssignmentResponseDto> getById(@PathVariable UUID id) {
        ProductTagAssignmentModel model = useCase.findById(id);
        return ResponseEntity.ok(mapper.toResponse(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTagAssignmentResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductTagAssignmentUpdateDto dto) {

        // Trae existente y aplica patch ignorando nulls
        ProductTagAssignmentModel existing = useCase.findById(id);
        mapper.updateModelFromDto(existing, dto);
        ProductTagAssignmentModel saved = useCase.update(id, existing);
        return ResponseEntity.ok(mapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        useCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            Pageable pageable,
            @RequestParam(required = false) Boolean enabled) {

        if (enabled == null) {
            var page = useCase.findAll(pageable);
            List<ProductTagAssignmentResponseDto> data =
                    page.map(mapper::toResponse).getContent();
            return ResponseEntity.ok(Map.of(
                    "data", data,
                    "total", page.getTotalElements()
            ));
        } else {
            List<ProductTagAssignmentResponseDto> data =
                    useCase.findByEnabled(enabled).stream()
                            .map(mapper::toResponse)
                            .toList();
            return ResponseEntity.ok(Map.of(
                    "data", data,
                    "total", (long) data.size()
            ));
        }
    }

    @GetMapping("/enabled")
    public ResponseEntity<Map<String, Object>> listEnabled() {
        List<ProductTagAssignmentResponseDto> data =
                useCase.findByEnabled(true).stream()
                        .map(mapper::toResponse)
                        .toList();
        return ResponseEntity.ok(Map.of("data", data, "total", data.size()));
    }


    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductTagAssignmentResponseDto> enable(@PathVariable UUID id) {
        ProductTagAssignmentModel enabled = useCase.enable(id);
        return ResponseEntity.ok(mapper.toResponse(enabled));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductTagAssignmentResponseDto> disable(@PathVariable UUID id) {
        ProductTagAssignmentModel disabled = useCase.disable(id);
        return ResponseEntity.ok(mapper.toResponse(disabled));
    }

    @GetMapping("/enabled/count")
    public Map<String, Long> countEnabled() {
        return Map.of("count", useCase.countEnabled());
    }
}