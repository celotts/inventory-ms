package com.celotts.purchaseservice.infrastructure.adapter.input.rest.controller;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseCreateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseResponseDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseUpdateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.mapper.PurchaseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Purchase API", description = "API for managing purchase orders and their lifecycle")
public class PurchaseController {

    private final PurchaseUseCase purchaseUseCase;
    private final PurchaseMapper purchaseMapper;

    @Operation(summary = "Create a new purchase order", description = "Creates a new purchase order. Validates the existence of the supplier and products before creation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., missing required fields, negative values)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Supplier or Product not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Purchase order number already exists", content = @Content)
    })
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

    @Operation(summary = "Get purchase by ID", description = "Retrieves the details of a specific purchase order by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDto> getById(
            @Parameter(description = "UUID of the purchase to retrieve", required = true)
            @PathVariable UUID id) {
        PurchaseModel purchase = purchaseUseCase.findById(id);
        return ResponseEntity.ok(purchaseMapper.toResponse(purchase));
    }

    @Operation(summary = "List all purchases", description = "Retrieves a paginated list of purchase orders. Supports sorting.")
    @ApiResponse(responseCode = "200", description = "List of purchases retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<PurchaseResponseDto>> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<PurchaseModel> purchasePage = purchaseUseCase.findAll(pageable);
        return ResponseEntity.ok(purchasePage.map(purchaseMapper::toResponse));
    }

    @Operation(summary = "Update a purchase", description = "Updates an existing purchase order. Only allows updates if the purchase is in a modifiable state (e.g., DRAFT).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., invalid state transition)", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseResponseDto> update(
            @Parameter(description = "UUID of the purchase to update", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid PurchaseUpdateDto updateDto) {
        PurchaseModel purchaseToUpdate = purchaseMapper.toModel(updateDto);
        PurchaseModel updatedPurchase = purchaseUseCase.update(id, purchaseToUpdate);
        return ResponseEntity.ok(purchaseMapper.toResponse(updatedPurchase));
    }

    @Operation(summary = "Delete a purchase", description = "Soft deletes a purchase order. The record remains in the database but is marked as deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Purchase deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID of the purchase to delete", required = true)
            @PathVariable UUID id) {
        purchaseUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
