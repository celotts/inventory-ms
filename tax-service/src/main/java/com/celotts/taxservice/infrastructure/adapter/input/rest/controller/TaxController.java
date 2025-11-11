package com.celotts.taxservice.infrastructure.adapter.input.rest.controller;

import com.celotts.taxservice.domain.model.TaxModel;
import com.celotts.taxservice.domain.port.input.TaxUseCase;
// IMPORTACIÓN DE LA EXCEPCIÓN PERSONALIZADA
import com.celotts.taxservice.domain.exception.ResourceNotFoundException;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.response.ApiResponse;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxCreateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxResponseDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxUpdateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.mapper.TaxMapper;
import com.celotts.taxservice.infrastructure.common.dto.PageableRequestDto;
import com.celotts.taxservice.infrastructure.common.util.PageableUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder; // Importación para el Locale de la petición
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tax")
@RequiredArgsConstructor
@Slf4j
public class TaxController {

    private final TaxUseCase taxUseCase;
    private final TaxMapper taxMapper;
    private final PageableUtils pageableUtils;
    private final MessageSource messageSource;


    // Método de utilidad para resolver mensajes de log (usando el Locale de la petición)
    private String getLogMessage(String key, Object... args) {
        // Usa LocaleContextHolder para asegurar que el idioma es el de la petición.
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /** Genera y lanza ResourceNotFoundException con mensaje localizado por ID. */
    private ResourceNotFoundException resourceNotFoundException(UUID id) {
        String resourceName = getLogMessage("log.tax");
        String connectorText = getLogMessage("log.with.id");
        String finalArgument = resourceName + connectorText + id;

        String errorMessage = getLogMessage(
                "error.resource.notfound",
                // CAMBIO: Pasar el argumento directamente
                finalArgument // Antes: new Object[]{finalArgument}
        );
        return new ResourceNotFoundException(errorMessage);
    }

    /** Genera y lanza ResourceNotFoundException con mensaje localizado por Nombre. */
    private ResourceNotFoundException resourceNotFoundException(String name) {
        String resourceName = getLogMessage("log.tax");
        String connectorText = getLogMessage("log.with.name");
        String finalArgument = resourceName + connectorText + name;

        String errorMessage = getLogMessage(
                "error.resource.notfound",
                // CAMBIO: Pasar el argumento directamente
                finalArgument // Antes: new Object[]{finalArgument}
        );
        return new ResourceNotFoundException(errorMessage);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<TaxResponseDto>> create(@Valid @RequestBody TaxCreateDto dto) {
        log.info(getLogMessage("log.tax.creating", dto.getName()));

        TaxModel model = taxMapper.createdFrom(dto);
        TaxModel saved = taxUseCase.create(model);
        TaxResponseDto response = taxMapper.toResponse(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxResponseDto>> findById(@PathVariable UUID id) {
        log.info(getLogMessage("log.tax.finding.byid", id));

        TaxModel model = taxUseCase.findById(id)
                // Llama al método que lanza ResourceNotFoundException(id)
                .orElseThrow(() -> resourceNotFoundException(id));

        TaxResponseDto response = taxMapper.toResponse(model);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<TaxResponseDto>> findByName(@PathVariable String name) {
        log.info(getLogMessage("log.tax.finding.byname", name));

        TaxModel model = taxUseCase.findByName(name)
                // Llama al método que lanza ResourceNotFoundException(name)
                .orElseThrow(() -> resourceNotFoundException(name));

        TaxResponseDto response = taxMapper.toResponse(model);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaxResponseDto>>> findAll(
            @Valid PageableRequestDto pageableDto) {
        log.info(getLogMessage("log.tax.finding.all.paginated"));

        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAll(pageable);
        List<TaxResponseDto> list = taxMapper.toResponseList(page.getContent());

        return ResponseEntity.ok(ApiResponse.okList(list));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TaxResponseDto>>> findByActive(
            @RequestParam Boolean active,
            @Valid PageableRequestDto pageableDto) {
        log.info(getLogMessage("log.tax.finding.byactive", active));

        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findByActive(active, pageable);
        List<TaxResponseDto> list = taxMapper.toResponseList(page.getContent());

        return ResponseEntity.ok(ApiResponse.okList(list));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TaxResponseDto>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @Valid PageableRequestDto pageableDto) {
        log.info(getLogMessage("log.tax.finding.paginated.filtered", name, active));

        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAllPaginated(name, active, pageable);
        List<TaxResponseDto> list = taxMapper.toResponseList(page.getContent());

        return ResponseEntity.ok(ApiResponse.okList(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxResponseDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaxUpdateDto dto) {
        log.info(getLogMessage("log.tax.saving", id));

        TaxModel model = taxUseCase.findById(id)
                // Llama al método que lanza ResourceNotFoundException(id)
                .orElseThrow(() -> resourceNotFoundException(id));

        taxMapper.updateFrom(dto, model);
        TaxModel updated = taxUseCase.save(model);
        TaxResponseDto response = taxMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        log.info(getLogMessage("log.tax.deleting", id));

        if (!taxUseCase.existById(id)) {
            // Lanza la excepción directamente
            throw resourceNotFoundException(id);
        }

        taxUseCase.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<TaxResponseDto>> activate(@PathVariable UUID id) {
        log.info(getLogMessage("log.tax.saving", id));

        TaxModel model = taxUseCase.findById(id)
                // Llama al método que lanza ResourceNotFoundException(id)
                .orElseThrow(() -> resourceNotFoundException(id));

        model.setIsActive(true);
        TaxModel updated = taxUseCase.save(model);
        TaxResponseDto response = taxMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<TaxResponseDto>> deactivate(@PathVariable UUID id) {
        log.info(getLogMessage("log.tax.saving", id));

        TaxModel model = taxUseCase.findById(id)
                // Llama al método que lanza ResourceNotFoundException(id)
                .orElseThrow(() -> resourceNotFoundException(id));

        model.setIsActive(false);
        TaxModel updated = taxUseCase.save(model);
        TaxResponseDto response = taxMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}