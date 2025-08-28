package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.domain.port.input.product.ProductPriceHistoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productPriceHistory.ProductPriceHistoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productPriceHistory.ProductPriceHistoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productPriceHistory.ProductPriceHistoryDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products/{productId}/prices")
@RequiredArgsConstructor
@Validated
public class ProductPriceHistoryController {

    private final ProductPriceHistoryUseCase useCase;
    private final ProductPriceHistoryDtoMapper mapper;

    /**
     * Crea un nuevo registro de histórico de precio para el producto.
     * - El productId del path es el autoritativo (se ignora el que venga en el body, si viene).
     * - enabled puede venir null -> el use case lo defaulta a true.
     * - changedAt lo setea el backend (use case) si viene null.
     */
    @PostMapping
    public ResponseEntity<ProductPriceHistoryResponseDto> create(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductPriceHistoryCreateDto dto
    ) {

        ProductPriceHistoryModel in = mapper.toModel(dto, productId);
        ProductPriceHistoryModel saved = useCase.create(in);
        ProductPriceHistoryResponseDto out = mapper.toResponse(saved);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(out.getId())
                .toUri();

        return ResponseEntity.created(location).body(out);
    }

    /**
     * Lista el histórico completo (ordenado desc por changedAt).
     */
    @GetMapping
    public ResponseEntity<List<ProductPriceHistoryResponseDto>> list(@PathVariable UUID productId) {
        List<ProductPriceHistoryResponseDto> out = useCase.getByProduct(productId)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(out);
    }

    /**
     * Devuelve el último precio (más reciente).
     */
    @GetMapping("/latest")
    public ResponseEntity<ProductPriceHistoryResponseDto> latest(@PathVariable UUID productId) {
        ProductPriceHistoryModel latest = useCase.getLatestPrice(productId);
        return ResponseEntity.ok(mapper.toResponse(latest));
    }
}