package com.celotts.taxservice.infrastructure.adapter.input.rest.controller;

import com.celotts.taxservice.domain.model.TaxModel;
import com.celotts.taxservice.domain.port.input.TaxUseCase;
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

    @PostMapping
    public ResponseEntity<ApiResponse<TaxResponseDto>> create(@Valid @RequestBody TaxCreateDto dto) {
        log.info("Creating new tax: {}", dto.getName());

        TaxModel model = taxMapper.createdFrom(dto);
        TaxModel saved = taxUseCase.create(model);
        TaxResponseDto response = taxMapper.toResponse(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxResponseDto>> findById(@PathVariable UUID id) {
        log.info("Finding tax by id: {}", id);

        TaxModel model = taxUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));
        TaxResponseDto response = taxMapper.toResponse(model);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<TaxResponseDto>> findByName(@PathVariable String name) {
        log.info("Finding tax by name: {}", name);

        TaxModel model = taxUseCase.findByName(name)
                .orElseThrow(() -> new RuntimeException("Tax not found with name: " + name));
        TaxResponseDto response = taxMapper.toResponse(model);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaxResponseDto>>> findAll(
            @Valid PageableRequestDto pageableDto) {
        log.info("Finding all taxes with pagination");

        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAll(pageable);
        List<TaxResponseDto> list = taxMapper.toResponseList(page.getContent());

        return ResponseEntity.ok(ApiResponse.okList(list));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TaxResponseDto>>> findByActive(
            @RequestParam Boolean active,
            @Valid PageableRequestDto pageableDto) {
        log.info("Finding taxes by active status: {}", active);

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
        log.info("Searching taxes - name: {}, active: {}", name, active);

        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAllPaginated(name, active, pageable);
        List<TaxResponseDto> list = taxMapper.toResponseList(page.getContent());

        return ResponseEntity.ok(ApiResponse.okList(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxResponseDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaxUpdateDto dto) {
        log.info("Updating tax: {}", id);

        TaxModel model = taxUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));
        taxMapper.updateFrom(dto, model);
        TaxModel updated = taxUseCase.save(model);
        TaxResponseDto response = taxMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        log.info("Deleting tax: {}", id);

        if (!taxUseCase.existById(id)) {
            throw new RuntimeException("Tax not found with id: " + id);
        }

        taxUseCase.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<TaxResponseDto>> activate(@PathVariable UUID id) {
        log.info("Activating tax: {}", id);

        TaxModel model = taxUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));
        model.setIsActive(true);
        TaxModel updated = taxUseCase.save(model);
        TaxResponseDto response = taxMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<TaxResponseDto>> deactivate(@PathVariable UUID id) {
        log.info("Deactivating tax: {}", id);

        TaxModel model = taxUseCase.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));
        model.setIsActive(false);
        TaxModel updated = taxUseCase.save(model);
        TaxResponseDto response = taxMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}