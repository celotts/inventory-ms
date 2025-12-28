package com.celotts.purchaseservice.infrastructure.adapter.input.rest.controller;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseUseCase purchaseUseCase;

    @PostMapping
    public ResponseEntity<PurchaseModel> create(@RequestBody PurchaseModel purchase) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseUseCase.create(purchase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseModel> getById(@PathVariable UUID id) {
        return purchaseUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseModel>> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(purchaseUseCase.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseModel> update(@PathVariable UUID id, @RequestBody PurchaseModel purchase) {
        return ResponseEntity.ok(purchaseUseCase.update(id, purchase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        purchaseUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}