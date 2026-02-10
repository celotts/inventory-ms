package com.celotts.taxservice.infrastructure.adapter.input.rest.controller.tax;

import com.celotts.taxservice.domain.model.tax.TaxModel;
import com.celotts.taxservice.domain.port.input.tax.TaxUseCase;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxCreateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxResponseDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxUpdateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.mapper.tax.TaxMapper;
import com.celotts.taxservice.infrastructure.common.dto.PageableRequestDto;
import com.celotts.taxservice.infrastructure.common.util.PageableUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/taxes") // Plural
@RequiredArgsConstructor
@Slf4j
public class TaxController {

    private final TaxUseCase taxUseCase;
    private final TaxMapper taxMapper;
    private final PageableUtils pageableUtils;

    @PostMapping
    public ResponseEntity<TaxResponseDto> create(@Valid @RequestBody TaxCreateDto dto) {
        log.info("Creating new tax with name: {}", dto.getName());
        TaxModel model = taxMapper.createdFrom(dto);
        TaxModel saved = taxUseCase.create(model);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(taxMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxResponseDto> findById(@PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        return ResponseEntity.ok(taxMapper.toResponse(model));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TaxResponseDto> findByName(@PathVariable String name) {
        TaxModel model = taxUseCase.findByName(name);
        return ResponseEntity.ok(taxMapper.toResponse(model));
    }

    @GetMapping
    public ResponseEntity<Page<TaxResponseDto>> findAll(
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAll(pageable);
        return ResponseEntity.ok(page.map(taxMapper::toResponse));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<TaxResponseDto>> findByActive(
            @RequestParam Boolean active,
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findByActive(active, pageable);
        return ResponseEntity.ok(page.map(taxMapper::toResponse));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TaxResponseDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAllPaginated(name, active, pageable);
        return ResponseEntity.ok(page.map(taxMapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaxUpdateDto dto) {
        TaxModel model = taxUseCase.findById(id);
        taxMapper.updateFrom(dto, model);
        TaxModel updated = taxUseCase.save(model);
        return ResponseEntity.ok(taxMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taxUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TaxResponseDto> activate(@PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        model.setIsActive(true);
        TaxModel updated = taxUseCase.save(model);
        return ResponseEntity.ok(taxMapper.toResponse(updated));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<TaxResponseDto> deactivate(@PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        model.setIsActive(false);
        TaxModel updated = taxUseCase.save(model);
        return ResponseEntity.ok(taxMapper.toResponse(updated));
    }
}
