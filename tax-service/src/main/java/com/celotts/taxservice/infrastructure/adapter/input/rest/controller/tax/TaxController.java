package com.celotts.taxservice.infrastructure.adapter.input.rest.controller.tax;

import com.celotts.taxservice.domain.model.tax.TaxModel;
import com.celotts.taxservice.domain.port.input.tax.TaxUseCase;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxCreateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxResponseDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxUpdateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.mapper.tax.TaxMapper;
import com.celotts.taxservice.infrastructure.common.dto.PageableRequestDto;
import com.celotts.taxservice.infrastructure.common.util.PageableUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/taxes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tax API", description = "API for managing tax configurations (VAT, etc.)")
public class TaxController {

    private final TaxUseCase taxUseCase;
    private final TaxMapper taxMapper;
    private final PageableUtils pageableUtils;

    @Operation(summary = "Create a new tax", description = "Creates a new tax configuration in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tax created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaxResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Tax code already exists", content = @Content)
    })
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

    @Operation(summary = "Get tax by ID", description = "Retrieves the details of a specific tax by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaxResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Tax not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaxResponseDto> findById(
            @Parameter(description = "UUID of the tax to retrieve", required = true)
            @PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        return ResponseEntity.ok(taxMapper.toResponse(model));
    }

    @Operation(summary = "Get tax by Name", description = "Retrieves a tax configuration by its name.")
    @GetMapping("/name/{name}")
    public ResponseEntity<TaxResponseDto> findByName(@PathVariable String name) {
        TaxModel model = taxUseCase.findByName(name);
        return ResponseEntity.ok(taxMapper.toResponse(model));
    }

    @Operation(summary = "List all taxes", description = "Retrieves a paginated list of all taxes.")
    @GetMapping
    public ResponseEntity<Page<TaxResponseDto>> findAll(
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAll(pageable);
        return ResponseEntity.ok(page.map(taxMapper::toResponse));
    }

    @Operation(summary = "List active taxes", description = "Retrieves a paginated list of taxes filtered by their active status.")
    @GetMapping("/active")
    public ResponseEntity<Page<TaxResponseDto>> findByActive(
            @Parameter(description = "Filter by active status (true/false)", required = true)
            @RequestParam Boolean active,
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findByActive(active, pageable);
        return ResponseEntity.ok(page.map(taxMapper::toResponse));
    }

    @Operation(summary = "Search taxes", description = "Search taxes by name and/or active status with pagination.")
    @GetMapping("/search")
    public ResponseEntity<Page<TaxResponseDto>> search(
            @Parameter(description = "Partial name to search for")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active,
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAllPaginated(name, active, pageable);
        return ResponseEntity.ok(page.map(taxMapper::toResponse));
    }

    @Operation(summary = "Update a tax", description = "Updates an existing tax configuration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax updated successfully"),
            @ApiResponse(responseCode = "404", description = "Tax not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaxResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaxUpdateDto dto) {
        TaxModel model = taxUseCase.findById(id);
        taxMapper.updateFrom(dto, model);
        TaxModel updated = taxUseCase.save(model);
        return ResponseEntity.ok(taxMapper.toResponse(updated));
    }

    @Operation(summary = "Delete a tax", description = "Permanently deletes a tax configuration.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taxUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate a tax", description = "Sets the status of a tax to active.")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<TaxResponseDto> activate(@PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        model.setIsActive(true);
        TaxModel updated = taxUseCase.save(model);
        return ResponseEntity.ok(taxMapper.toResponse(updated));
    }

    @Operation(summary = "Deactivate a tax", description = "Sets the status of a tax to inactive.")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<TaxResponseDto> deactivate(@PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        model.setIsActive(false);
        TaxModel updated = taxUseCase.save(model);
        return ResponseEntity.ok(taxMapper.toResponse(updated));
    }
}
