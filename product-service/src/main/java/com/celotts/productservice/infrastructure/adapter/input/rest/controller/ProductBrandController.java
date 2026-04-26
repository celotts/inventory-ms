package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.domain.port.input.product.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.ApiResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productbrand.ProductBrandMapper;
import com.celotts.productservice.infrastructure.common.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/product-brands", produces = "application/json")
@Tag(name = "${swagger.product-brand.api.name}", description = "${swagger.product-brand.api.desc}")
public class ProductBrandController {

    private final ProductBrandUseCase productBrandUseCase;
    private final ProductBrandMapper productBrandMapper;
    private final MessageSource messageSource;

    private String msg(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @PostMapping(consumes = "application/json")
    @Operation(summary = "${swagger.product-brand.create.summary}")
    public ResponseEntity<ApiResult<ProductBrandResponseDto>> create(@Valid @RequestBody ProductBrandCreateDto dto) {
        ProductBrandModel model = productBrandMapper.toModel(dto);
        ProductBrandModel saved = productBrandUseCase.save(model);
        ProductBrandResponseDto response = productBrandMapper.toResponse(saved);

        URI location = URI.create("/api/v1/product-brands/" + response.getId());

        return ApiResult.created(response, msg("brand.created"), location);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    @Operation(summary = "${swagger.product-brand.update.summary}")
    public ResponseEntity<ApiResult<ProductBrandResponseDto>> update(@PathVariable UUID id,
                                                          @Valid @RequestBody ProductBrandUpdateDto dto) {
        log.info("PATCH brand id={} payload={}", id, dto);

        ProductBrandModel patch = ProductBrandModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())
                .build();

        ProductBrandModel updated = productBrandUseCase.update(id, patch);
        return ApiResult.success(productBrandMapper.toResponse(updated), msg("brand.updated"));
    }

    @GetMapping
    @Operation(summary = "${swagger.product-brand.list.summary}")
    public ResponseEntity<ApiResult<List<ProductBrandResponseDto>>> getAllBrands() {
        List<ProductBrandResponseDto> list = productBrandUseCase.findAll()
                .stream()
                .map(productBrandMapper::toResponse)
                .toList();

        return ApiResult.success(list, msg("brand.list"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "${swagger.product-brand.delete.summary}")
    public ResponseEntity<ApiResult<Void>> deleteBrand(@PathVariable UUID id,
                                            @RequestParam String deletedBy,
                                            @RequestParam String reason) {
        productBrandUseCase.deleteById(id, deletedBy, reason);
        return ApiResult.success(null, msg("brand.deleted"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "${swagger.product-brand.get-by-id.summary}")
    public ResponseEntity<ApiResult<ProductBrandResponseDto>> getById(@PathVariable UUID id) {
        ProductBrandModel model = productBrandUseCase.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("brand.not-found", id));
        
        return ApiResult.success(productBrandMapper.toResponse(model), msg("brand.found"));
    }

    @GetMapping("/{id}/name")
    @Operation(summary = "${swagger.product-brand.get-name-by-id.summary}")
    public ResponseEntity<ApiResult<String>> getBrandNameById(@PathVariable UUID id) {
        String name = productBrandUseCase.findNameById(id)
                .orElseThrow(() -> new ResourceNotFoundException("brand.name.not-found", id));
        
        return ApiResult.success(name, msg("brand.found"));
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "${swagger.product-brand.enable.summary}")
    public ResponseEntity<ApiResult<ProductBrandResponseDto>> enableBrand(@PathVariable UUID id) {
        ProductBrandModel brand = productBrandUseCase.enableBrand(id);
        return ApiResult.success(productBrandMapper.toResponse(brand), msg("brand.updated"));
    }

    @PatchMapping("/{id}/disable")
    @Operation(summary = "${swagger.product-brand.disable.summary}")
    public ResponseEntity<ApiResult<ProductBrandResponseDto>> disableBrand(@PathVariable UUID id) {
        ProductBrandModel brand = productBrandUseCase.disableBrand(id);
        return ApiResult.success(productBrandMapper.toResponse(brand), msg("brand.updated"));
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @Operation(summary = "${swagger.product-brand.replace.summary}")
    public ResponseEntity<ApiResult<ProductBrandResponseDto>> replace(@PathVariable UUID id,
                                                           @Valid @RequestBody ProductBrandUpdateDto dto) {

        ProductBrandModel patch = ProductBrandModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .enabled(dto.getEnabled())
                .updatedBy(dto.getUpdatedBy())
                .build();
        ProductBrandModel updated = productBrandUseCase.update(id, patch);
        return ApiResult.success(productBrandMapper.toResponse(updated), msg("brand.updated"));
    }
}
