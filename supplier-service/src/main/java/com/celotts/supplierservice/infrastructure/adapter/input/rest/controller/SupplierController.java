package com.celotts.supplierservice.infrastructure.adapter.input.rest.controller;

import com.celotts.supplierservice.domain.port.input.SupplierUseCase;
import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierCreateDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDeleteDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierResponseDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierUpdateDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.supplier.SupplierMapper;
import com.celotts.supplierservice.infrastructure.common.dto.PageableRequestDto;
import com.celotts.supplierservice.infrastructure.common.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Supplier API", description = "API for managing suppliers")
public class SupplierController {

    private final SupplierUseCase useCase;
    private final SupplierMapper mapper;
    private final MessageSource messageSource;

    // --- 🔍 VALIDACIÓN INTER-SERVICE ---

    @Operation(summary = "Check if supplier exists", description = "Checks if a supplier exists by its ID. Used mainly by other microservices.")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID id) {
        return ResponseEntity.ok(useCase.existsById(id));
    }

    // --- 🏗️ CREACIÓN ---

    @Operation(summary = "Create a new supplier", description = "Creates a new supplier in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupplierResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Supplier code or tax ID already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SupplierResponseDto> create(
            @Validated(ValidationGroups.Create.class) @RequestBody SupplierCreateDto body
    ) {
        body.normalizeFields();
        SupplierModel created = useCase.create(mapper.toModel(body));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        log.info("URI generada para Location: {}", location);

        return ResponseEntity.created(location).body(mapper.toResponse(created));
    }

    // --- 📖 LECTURA Y LISTADO ---

    @Operation(summary = "Get supplier by ID", description = "Retrieves the details of a specific supplier by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found"),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> getById(@PathVariable UUID id) {
        SupplierModel model = useCase.getById(id);
        return ResponseEntity.ok(mapper.toResponse(model));
    }

    @Operation(summary = "Get supplier by Code", description = "Retrieves a supplier by its unique code.")
    @GetMapping("/code/{code}")
    public ResponseEntity<SupplierResponseDto> getByCode(@PathVariable String code) {
        SupplierModel model = useCase.getByCode(code);
        return ResponseEntity.ok(mapper.toResponse(model));
    }

    @Operation(summary = "List suppliers", description = "Retrieves a paginated list of suppliers. Supports filtering by name and active status.")
    @GetMapping
    public ResponseEntity<Page<SupplierResponseDto>> list(
            @Valid @ModelAttribute PageableRequestDto pageReq,
            @Parameter(description = "Filter by partial name") @RequestParam(name = "q", required = false) String q,
            @Parameter(description = "Filter by active status") @RequestParam(name = "active", required = false) Boolean active
    ) {
        Pageable pageable = PageRequest.of(pageReq.getPageOrDefault(), pageReq.getSizeOrDefault(), pageReq.toSort());

        Page<SupplierModel> page;
        if (q != null && !q.isBlank()) {
            page = useCase.findByNameContaining(q.trim(), pageable);
        } else if (active != null) {
            page = useCase.findByActive(active);
        } else {
            page = useCase.findAll(pageable);
        }
        return ResponseEntity.ok(mapper.toResponsePage(page));
    }

    // --- 🛠️ BÚSQUEDAS Y SUGERENCIAS ---

    @Operation(summary = "Check existence by name", description = "Checks if a supplier exists by name.")
    @GetMapping("/_exists")
    public ResponseEntity<Map<String, Object>> existsByName(
            @RequestParam @NotBlank(message = "{validation.field-error}") String name
    ) {
        return ResponseEntity.ok(Map.of(
                "exists", useCase.existsByName(name),
                "name", name
        ));
    }

    @Operation(summary = "Check existence by code", description = "Checks if a supplier exists by code.")
    @GetMapping("/_exists-code")
    public ResponseEntity<Map<String, Object>> existsByCode(@RequestParam String code) {
        return ResponseEntity.ok(Map.of(
                "exists", useCase.existsByCode(code),
                "code", code
        ));
    }

    @Operation(summary = "Suggest suppliers", description = "Provides a list of supplier suggestions based on a search query.")
    @GetMapping("/_suggest")
    public ResponseEntity<List<SupplierResponseDto>> suggest(
            @RequestParam(name = "q") @NotBlank(message = "{validation.field-error}") String q,
            @RequestParam(name = "limit", required = false, defaultValue = "10") @Positive @Min(1) int limit
    ) {
        List<SupplierModel> list = useCase.searchByNameDescription(q, Math.min(limit, 1000));
        return ResponseEntity.ok(mapper.toResponseList(list));
    }

    // --- ✍️ ACTUALIZACIÓN Y BORRADO ---

    @Operation(summary = "Update a supplier", description = "Updates an existing supplier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> update(
            @PathVariable UUID id,
            @Validated(ValidationGroups.Update.class) @RequestBody SupplierUpdateDto body
    ) {
        body.normalizeFields();
        SupplierModel updated = useCase.update(id, mapper.toModel(body));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Operation(summary = "Delete a supplier", description = "Soft deletes a supplier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @RequestBody(required = false) SupplierDeleteDto body
    ) {
        String deletedBy = (body != null) ? body.getDeletedBy() : null;
        String reason = (body != null) ? body.getReason() : null;

        useCase.delete(id, deletedBy, reason);
        return ResponseEntity.noContent().build();
    }

    // --- 🏥 SALUD ---

    @Operation(summary = "Ping service", description = "Simple ping to check service status.")
    @GetMapping("/_ping")
    public ResponseEntity<Map<String, Object>> ping() {
        return ResponseEntity.ok(Map.of(
                "service", "supplier-service",
                "status", "OK - VERSION 3 - PRUEBA FINAL"
        ));
    }
}
