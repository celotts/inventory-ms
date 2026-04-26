package com.celotts.purchaseservice.infrastructure.adapter.input.rest.controller;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import com.celotts.purchaseservice.domain.port.input.ReceivePurchaseUseCase;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseCreateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseResponseDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.PurchaseUpdateDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.mapper.PurchaseMapper;
import com.celotts.purchaseservice.infrastructure.common.ApiResult;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
@Tag(name = "${swagger.purchase.api.name}", description = "${swagger.purchase.api.desc}")
public class PurchaseController {

    private final PurchaseUseCase purchaseUseCase;
    private final ReceivePurchaseUseCase receivePurchaseUseCase;
    private final PurchaseMapper purchaseMapper;
    private final MessageSource messageSource;

    private String msg(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @Operation(summary = "${swagger.purchase.create.summary}", description = "${swagger.purchase.create.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Supplier or Product not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Purchase order number already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResult<PurchaseResponseDto>> create(@RequestBody @Valid PurchaseCreateDto createDto) {
        log.info("Creating new purchase with order number: {}", createDto.getOrderNumber());
        PurchaseModel purchaseToCreate = purchaseMapper.toModel(createDto);
        PurchaseModel createdPurchase = purchaseUseCase.create(purchaseToCreate);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPurchase.getId())
                .toUri();

        return ApiResult.created(purchaseMapper.toResponse(createdPurchase), msg("purchase.created"), location);
    }

    @Operation(summary = "${swagger.purchase.receive.summary}", description = "${swagger.purchase.receive.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase received successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid state for receiving (must be PLACED or DRAFT)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content)
    })
    @PostMapping("/{id}/receive")
    public ResponseEntity<ApiResult<PurchaseResponseDto>> receive(
            @Parameter(description = "UUID of the purchase to receive", required = true)
            @PathVariable UUID id) {
        PurchaseModel receivedPurchase = receivePurchaseUseCase.receive(id);
        return ApiResult.success(purchaseMapper.toResponse(receivedPurchase), msg("purchase.received"));
    }

    @Operation(summary = "${swagger.purchase.get-by-id.summary}", description = "${swagger.purchase.get-by-id.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<PurchaseResponseDto>> getById(
            @Parameter(description = "UUID of the purchase to retrieve", required = true)
            @PathVariable UUID id) {
        PurchaseModel purchase = purchaseUseCase.findById(id);
        return ApiResult.success(purchaseMapper.toResponse(purchase), msg("purchase.found"));
    }

    @Operation(summary = "${swagger.purchase.list.summary}", description = "${swagger.purchase.list.desc}")
    @ApiResponse(responseCode = "200", description = "List of purchases retrieved successfully")
    @GetMapping
    public ResponseEntity<ApiResult<Page<PurchaseResponseDto>>> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<PurchaseModel> purchasePage = purchaseUseCase.findAll(pageable);
        return ApiResult.success(purchasePage.map(purchaseMapper::toResponse), msg("purchase.list"));
    }

    @Operation(summary = "${swagger.purchase.update.summary}", description = "${swagger.purchase.update.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., invalid state transition)", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<PurchaseResponseDto>> update(
            @Parameter(description = "UUID of the purchase to update", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid PurchaseUpdateDto updateDto) {
        PurchaseModel purchaseToUpdate = purchaseMapper.toModel(updateDto);
        PurchaseModel updatedPurchase = purchaseUseCase.update(id, purchaseToUpdate);
        return ApiResult.success(purchaseMapper.toResponse(updatedPurchase), msg("purchase.updated"));
    }

    @Operation(summary = "${swagger.purchase.delete.summary}", description = "${swagger.purchase.delete.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Purchase deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "UUID of the purchase to delete", required = true)
            @PathVariable UUID id) {
        purchaseUseCase.delete(id);
        return ApiResult.success(null, msg("purchase.deleted"));
    }
}
