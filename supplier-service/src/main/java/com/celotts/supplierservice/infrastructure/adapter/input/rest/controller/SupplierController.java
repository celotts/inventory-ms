package com.celotts.supplierservice.infrastructure.adapter.input.rest.controller;

import com.celotts.supplierservice.domain.port.input.SupplierUseCase;
import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierCreateDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDeleteDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierResponseDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierUpdateDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.supplier.SupplierMapper;
import com.celotts.supplierservice.infrastructure.common.ApiResult;
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
@Tag(name = "${swagger.supplier.api.name}", description = "${swagger.supplier.api.desc}")
public class SupplierController {

    private final SupplierUseCase useCase;
    private final SupplierMapper mapper;
    private final MessageSource messageSource;

    private String msg(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    // --- 🔍 VALIDACIÓN INTER-SERVICE ---

    @Operation(summary = "${swagger.supplier.exists.summary}", description = "${swagger.supplier.exists.desc}")
    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResult<Boolean>> existsById(@PathVariable UUID id) {
        return ApiResult.success(useCase.existsById(id), msg("supplier.exists"));
    }

    // --- 🏗️ CREACIÓN ---

    @Operation(summary = "${swagger.supplier.create.summary}", description = "${swagger.supplier.create.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupplierResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Supplier code or tax ID already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResult<SupplierResponseDto>> create(
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

        return ApiResult.created(mapper.toResponse(created), msg("supplier.created"), location);
    }

    // --- 📖 LECTURA Y LISTADO ---

    @Operation(summary = "${swagger.supplier.get-by-id.summary}", description = "${swagger.supplier.get-by-id.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found"),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<SupplierResponseDto>> getById(@PathVariable UUID id) {
        SupplierModel model = useCase.getById(id);
        return ApiResult.success(mapper.toResponse(model), msg("supplier.found"));
    }

    @Operation(summary = "${swagger.supplier.get-by-code.summary}", description = "${swagger.supplier.get-by-code.desc}")
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResult<SupplierResponseDto>> getByCode(@PathVariable String code) {
        SupplierModel model = useCase.getByCode(code);
        return ApiResult.success(mapper.toResponse(model), msg("supplier.found"));
    }

    @Operation(summary = "${swagger.supplier.list.summary}", description = "${swagger.supplier.list.desc}")
    @GetMapping
    public ResponseEntity<ApiResult<Page<SupplierResponseDto>>> list(
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
        return ApiResult.success(mapper.toResponsePage(page), msg("supplier.list"));
    }

    // --- 🛠️ BÚSQUEDAS Y SUGERENCIAS ---

    @Operation(summary = "${swagger.supplier.exists-by-name.summary}", description = "${swagger.supplier.exists-by-name.desc}")
    @GetMapping("/_exists")
    public ResponseEntity<ApiResult<Map<String, Object>>> existsByName(
            @RequestParam @NotBlank(message = "{validation.field-error}") String name
    ) {
        return ApiResult.success(Map.of(
                "exists", useCase.existsByName(name),
                "name", name
        ), msg("supplier.exists"));
    }

    @Operation(summary = "${swagger.supplier.exists-by-code.summary}", description = "${swagger.supplier.exists-by-code.desc}")
    @GetMapping("/_exists-code")
    public ResponseEntity<ApiResult<Map<String, Object>>> existsByCode(@RequestParam String code) {
        return ApiResult.success(Map.of(
                "exists", useCase.existsByCode(code),
                "code", code
        ), msg("supplier.exists"));
    }

    @Operation(summary = "${swagger.supplier.suggest.summary}", description = "${swagger.supplier.suggest.desc}")
    @GetMapping("/_suggest")
    public ResponseEntity<ApiResult<List<SupplierResponseDto>>> suggest(
            @RequestParam(name = "q") @NotBlank(message = "{validation.field-error}") String q,
            @RequestParam(name = "limit", required = false, defaultValue = "10") @Positive @Min(1) int limit
    ) {
        List<SupplierModel> list = useCase.searchByNameDescription(q, Math.min(limit, 1000));
        return ApiResult.success(mapper.toResponseList(list), msg("supplier.suggestions"));
    }

    // --- ✍️ ACTUALIZACIÓN Y BORRADO ---

    @Operation(summary = "${swagger.supplier.update.summary}", description = "${swagger.supplier.update.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<SupplierResponseDto>> update(
            @PathVariable UUID id,
            @Validated(ValidationGroups.Update.class) @RequestBody SupplierUpdateDto body
    ) {
        body.normalizeFields();
        SupplierModel updated = useCase.update(id, mapper.toModel(body));
        return ApiResult.success(mapper.toResponse(updated), msg("supplier.updated"));
    }

    @Operation(summary = "${swagger.supplier.delete.summary}", description = "${swagger.supplier.delete.desc}")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(
            @PathVariable UUID id,
            @RequestBody(required = false) SupplierDeleteDto body
    ) {
        String deletedBy = (body != null) ? body.getDeletedBy() : null;
        String reason = (body != null) ? body.getReason() : null;

        useCase.delete(id, deletedBy, reason);
        return ApiResult.success(null, msg("supplier.deleted"));
    }

    // --- 🏥 SALUD ---

    @Operation(summary = "${swagger.supplier.ping.summary}", description = "${swagger.supplier.ping.desc}")
    @GetMapping("/_ping")
    public ResponseEntity<ApiResult<Map<String, Object>>> ping() {
        return ApiResult.success(Map.of(
                "service", "supplier-service",
                "status", messageSource.getMessage("app.status.ok", null, LocaleContextHolder.getLocale())
        ), "OK");
    }
}
