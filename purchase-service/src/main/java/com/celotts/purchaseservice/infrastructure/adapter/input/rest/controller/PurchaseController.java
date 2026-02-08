package com.celotts.purchaseservice.infrastructure.adapter.input.rest.controller;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseCreateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseResponseDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseUpdateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.mapper.PurchaseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchases") // Plural para seguir convenciones REST
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

    private final PurchaseUseCase purchaseUseCase;
    private final PurchaseMapper purchaseMapper;

    @PostMapping
    public ResponseEntity<PurchaseResponseDto> create(@RequestBody @Valid PurchaseCreateDto createDto) {
        log.info("Creating new purchase with order number: {}", createDto.getOrderNumber());
        PurchaseModel purchaseToCreate = purchaseMapper.toModel(createDto);
        PurchaseModel createdPurchase = purchaseUseCase.create(purchaseToCreate);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPurchase.getId())
                .toUri();

        return ResponseEntity.created(location).body(purchaseMapper.toResponse(createdPurchase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDto> getById(@PathVariable UUID id) {
        PurchaseModel purchase = purchaseUseCase.findById(id);
        return ResponseEntity.ok(purchaseMapper.toResponse(purchase));
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseResponseDto>> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<PurchaseModel> purchasePage = purchaseUseCase.findAll(pageable);
        return ResponseEntity.ok(purchasePage.map(purchaseMapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseResponseDto> update(@PathVariable UUID id, @RequestBody @Valid PurchaseUpdateDto updateDto) {
        PurchaseModel purchaseToUpdate = purchaseMapper.toModel(updateDto);
        PurchaseModel updatedPurchase = purchaseUseCase.update(id, purchaseToUpdate);
        return ResponseEntity.ok(purchaseMapper.toResponse(updatedPurchase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        purchaseUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
