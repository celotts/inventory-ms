package com.celotts.salesservice.infrastructure.adapter.input.rest.controller;

import com.celotts.salesservice.domain.model.SaleModel;
import com.celotts.salesservice.domain.port.input.SaleUseCase;
import com.celotts.salesservice.infrastructure.adapter.input.rest.dto.SaleCreateDto;
import com.celotts.salesservice.infrastructure.adapter.input.rest.mapper.SaleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Tag(name = "Sales API", description = "Operaciones de venta y POS")
public class SaleController {

    private final SaleUseCase saleUseCase;
    private final SaleMapper saleMapper;

    @PostMapping
    @Operation(summary = "Registrar una nueva venta", description = "Crea la venta, genera la comanda y descuenta stock")
    public ResponseEntity<SaleModel> createSale(@Valid @RequestBody SaleCreateDto createDto) {
        SaleModel saleModel = saleMapper.toModel(createDto);
        SaleModel savedSale = saleUseCase.createSale(saleModel);
        return new ResponseEntity<>(savedSale, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas las ventas")
    public ResponseEntity<List<SaleModel>> getAllSales() {
        return ResponseEntity.ok(saleUseCase.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de una venta")
    public ResponseEntity<SaleModel> getSaleById(@PathVariable UUID id) {
        return ResponseEntity.ok(saleUseCase.findById(id));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Marcar venta como completada")
    public ResponseEntity<Void> completeSale(@PathVariable UUID id) {
        saleUseCase.completeSale(id);
        return ResponseEntity.noContent().build();
    }
}