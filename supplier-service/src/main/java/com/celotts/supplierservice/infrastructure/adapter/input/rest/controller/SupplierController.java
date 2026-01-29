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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Validated
public class SupplierController {

    private final SupplierUseCase useCase;
    private final SupplierMapper mapper;
    private final MessageSource messageSource;

    // --- 🔍 VALIDACIÓN INTER-SERVICE ---

    /**
     * Endpoint optimizado para que otros microservicios (como Purchase) validen existencia.
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID id) {
        return ResponseEntity.ok(useCase.existsById(id));
    }

    // --- 🏗️ CREACIÓN ---

    @PostMapping
    public ResponseEntity<SupplierResponseDto> create(
            @Validated(ValidationGroups.Create.class) @RequestBody SupplierCreateDto body
    ) {
        body.normalizeFields();
        SupplierModel created = useCase.create(mapper.toModel(body));

        URI location = URI.create("/api/v1/suppliers/" + created.getId());
        return ResponseEntity.created(location).body(mapper.toResponse(created));
    }

    // --- 📖 LECTURA Y LISTADO ---

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> getById(@PathVariable UUID id) {
        SupplierModel model = useCase.getById(id);
        return ResponseEntity.ok(mapper.toResponse(model));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<SupplierResponseDto> getByCode(@PathVariable String code) {
        SupplierModel model = useCase.getByCode(code);
        return ResponseEntity.ok(mapper.toResponse(model));
    }

    @GetMapping
    public ResponseEntity<Page<SupplierResponseDto>> list(
            @Valid @ModelAttribute PageableRequestDto pageReq,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "active", required = false) Boolean active
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

    @GetMapping("/_exists")
    public ResponseEntity<Map<String, Object>> existsByName(
            @RequestParam @NotBlank(message = "{validation.field-error}") String name
    ) {
        return ResponseEntity.ok(Map.of(
                "exists", useCase.existsByName(name),
                "name", name
        ));
    }

    @GetMapping("/_exists-code")
    public ResponseEntity<Map<String, Object>> existsByCode(@RequestParam String code) {
        return ResponseEntity.ok(Map.of(
                "exists", useCase.existsByCode(code),
                "code", code
        ));
    }

    @GetMapping("/_suggest")
    public ResponseEntity<List<SupplierResponseDto>> suggest(
            @RequestParam(name = "q") @NotBlank(message = "{validation.field-error}") String q,
            @RequestParam(name = "limit", required = false, defaultValue = "10") @Positive @Min(1) int limit
    ) {
        List<SupplierModel> list = useCase.searchByNameDescription(q, Math.min(limit, 1000));
        return ResponseEntity.ok(mapper.toResponseList(list));
    }

    // --- ✍️ ACTUALIZACIÓN Y BORRADO ---

    @PatchMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> update(
            @PathVariable UUID id,
            @Validated(ValidationGroups.Update.class) @RequestBody SupplierUpdateDto body
    ) {
        body.normalizeFields();
        SupplierModel updated = useCase.update(id, mapper.toModel(body));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

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

    @GetMapping("/_ping")
    public ResponseEntity<Map<String, Object>> ping() {
        String okMsg = messageSource.getMessage("app.error.unexpected", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(Map.of(
                "service", "supplier-service",
                "status", "OK",
                "i18n.sample", okMsg
        ));
    }
}