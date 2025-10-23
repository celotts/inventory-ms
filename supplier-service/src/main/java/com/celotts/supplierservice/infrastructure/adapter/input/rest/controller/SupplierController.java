package com.celotts.supplierservice.infrastructure.adapter.input.rest.controller;

import com.celotts.supplierservice.domain.port.input.supplier.SupplierUseCase;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.response.ApiErrorResponse;
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

    private final SupplierUseCase useCase;          // Capa aplicación
    private final SupplierMapper mapper;            // MapStruct (DTO <-> Domain)
    private final MessageSource messageSource;      // Para textos i18n en respuestas simples

    // -------------------- Create --------------------
    @PostMapping
    public ResponseEntity<SupplierResponseDto> create(
            @Validated(ValidationGroups.Create.class) @RequestBody SupplierCreateDto body
    ) {
        body.normalizeFields();

        SupplierModel toCreate = mapper.toModel(body);
        SupplierModel created = useCase.create(toCreate);
        SupplierResponseDto resp = mapper.toResponse(created);

        URI location = URI.create("/api/v1/suppliers/" + created.getId());
        return ResponseEntity.created(location).body(resp);
    }

    // -------------------- Get by ID --------------------
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> getById(@PathVariable UUID id) {
        SupplierModel model = useCase.getById(id); // lanza SupplierNotFoundException si no existe
        return ResponseEntity.ok(mapper.toResponse(model));
    }

    // -------------------- Exists by name --------------------
    @GetMapping("/_exists")
    public ResponseEntity<Map<String, Object>> existsByName(
            @RequestParam @NotBlank(message = "{validation.field-error}") String name
    ) {
        boolean exists = useCase.existsByName(name);
        Map<String, Object> payload = new HashMap<>();
        payload.put("exists", exists);
        payload.put("name", name);
        return ResponseEntity.ok(payload);
    }

    // -------------------- Search suggestions (q + limit) --------------------
    @GetMapping("/_suggest")
    public ResponseEntity<List<SupplierResponseDto>> suggest(
            @RequestParam(name = "q") @NotBlank(message = "{validation.field-error}") String q,
            @RequestParam(name = "limit", required = false, defaultValue = "10") @Positive @Min(1) int limit
    ) {

        List<SupplierModel> list = useCase.searchByNameDescription(q, Math.min(limit, 1000));
        return ResponseEntity.ok(mapper.toResponseList(list));
    }

    // -------------------- List / Page (opcionalmente filtrado por q o active) --------------------
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

    // -------------------- Partial Update (PATCH) --------------------
    @PatchMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> update(
            @PathVariable UUID id,
            @Validated(ValidationGroups.Update.class) @RequestBody SupplierUpdateDto body
    ) {
        body.normalizeFields();

        SupplierModel toUpdate = mapper.toModel(body);
        SupplierModel updated = useCase.update(id, toUpdate);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    // -------------------- Delete (soft delete con metadata opcional) --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @RequestBody(required = false) SupplierDeleteDto body
    ) {
        String deletedBy = body != null ? body.getDeletedBy() : null;
        String reason = body != null ? body.getReason() : null;

        useCase.delete(id, deletedBy, reason);
        return ResponseEntity.noContent().build();
    }

    // -------------------- Ping / health simple --------------------
    @GetMapping("/_ping")
    public ResponseEntity<Map<String, Object>> ping() {
        var ok = messageSource.getMessage("app.error.unexpected", null, LocaleContextHolder.getLocale()); // solo muestra i18n está disponible
        Map<String, Object> payload = Map.of(
                "service", "supplier-service",
                "status", "OK",
                "i18n.sample", ok
        );
        return ResponseEntity.ok(payload);
    }

    // (Opcional) Ejemplo de respuesta de error estándar manual
    @GetMapping("/_force-400")
    public ResponseEntity<ApiErrorResponse> forceBadRequest() {
        var msg = messageSource.getMessage("validation.failed.title", null, LocaleContextHolder.getLocale());
        ApiErrorResponse error = new ApiErrorResponse(
                java.time.LocalDateTime.now(),
                400,
                msg,
                "demo error",
                "/api/v1/suppliers/_force-400"
        );
        return ResponseEntity.badRequest().body(error);
    }

    // GET /api/v1/suppliers/code/{code}
    @GetMapping("/code/{code}")
    public ResponseEntity<SupplierResponseDto> getByCode(
            @PathVariable /*@CodeFormat*/ String code // si quieres, valida con tu anotación custom
    ) {
        SupplierModel model = useCase.getByCode(code); // lanza SupplierNotFoundException si no existe
        return ResponseEntity.ok(mapper.toResponse(model));
    }

    // GET /api/v1/suppliers/_exists-code?code=ABC-123
    @GetMapping("/_exists-code")
    public ResponseEntity<Map<String, Object>> existsByCode(
            @RequestParam("code") /*@CodeFormat*/ String code
    ) {
        boolean exists = useCase.existsByCode(code);
        return ResponseEntity.ok(Map.of("exists", exists, "code", code));
    }
}