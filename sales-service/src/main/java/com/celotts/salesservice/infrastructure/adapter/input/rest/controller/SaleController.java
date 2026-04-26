package com.celotts.salesservice.infrastructure.adapter.input.rest.controller;

import com.celotts.salesservice.domain.model.SaleModel;
import com.celotts.salesservice.domain.model.SaleStatus;
import com.celotts.salesservice.domain.model.OrderSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Tag(name = "Sales API", description = "Endpoints for managing restaurant sales and POS")
public class SaleController {

    @PostMapping
    public ResponseEntity<SaleModel> createSale(@RequestBody SaleModel sale) {
        // Lógica para guardar la venta, descontar stock y generar comanda
        sale.setId(UUID.randomUUID());
        sale.setStatus(SaleStatus.PENDING);
        return ResponseEntity.ok(sale);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleModel> getSale(@PathVariable UUID id) {
        return ResponseEntity.ok(new SaleModel());
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeSale(@PathVariable UUID id) {
        return ResponseEntity.noContent().build();
    }
}