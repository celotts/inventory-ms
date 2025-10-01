package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.domain.port.input.product.ProductTypeUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeRequestDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype.ProductTypeResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.producttype.ProductTypeMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product-type")
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Slf4j
public class ProductTypeController {
    private final ProductTypeUseCase productTypeUseCase;
    private final ProductTypeMapper mapper;

    @PostMapping
    public ResponseEntity<ProductTypeResponseDto> create(@Valid @RequestBody ProductTypeCreateDto dto) {
        ProductTypeModel toCreate = mapper.toModel(dto);
        ProductTypeModel created = productTypeUseCase.create(toCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/product-types/" + created.getId())
                .body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeResponseDto> getById(@PathVariable UUID id) {
        return productTypeUseCase.getById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductTypeResponseDto> getByCode(@PathVariable String code) {
        return productTypeUseCase.getByCode(code)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductTypeResponseDto>> getAllPaginated(ProductTypeRequestDto requestDto) {
        Pageable pageable = PageRequest.of(
                requestDto.getPageOrDefault(),
                requestDto.getSizeOrDefault(),
                requestDto.toSort()
        );

        Page<ProductTypeModel> models = productTypeUseCase.getAll(pageable /* + filtros extra */);

        Page<ProductTypeResponseDto> dtoPage = models.map(mapper::toResponse);
        return ResponseEntity.ok(dtoPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeResponseDto> update (
        @PathVariable UUID id,
        @Valid @RequestBody ProductTypeCreateDto dto
    ) {
        ProductTypeModel toUpdate = mapper.toModel(dto);
        ProductTypeModel updated = productTypeUseCase.update(id, toUpdate);

        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productTypeUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}