package com.celotts.taxservice.infrastructure.adapter.input.rest.controller.tax;

import com.celotts.taxservice.domain.model.tax.TaxModel;
import com.celotts.taxservice.domain.port.input.tax.TaxUseCase;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxCreateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxResponseDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxUpdateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.mapper.tax.TaxMapper;
import com.celotts.taxservice.infrastructure.common.ApiResult;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
@Tag(name = "${swagger.tax.api.name}", description = "${swagger.tax.api.desc}")
public class TaxController {

    private final TaxUseCase taxUseCase;
    private final TaxMapper taxMapper;
    private final PageableUtils pageableUtils;
    private final MessageSource messageSource;

    private String msg(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @Operation(summary = "${swagger.tax.create.summary}", description = "${swagger.tax.create.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tax created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaxResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Tax code already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResult<TaxResponseDto>> create(@Valid @RequestBody TaxCreateDto dto) {
        log.info("Creating new tax with name: {}", dto.getName());
        TaxModel model = taxMapper.createdFrom(dto);
        TaxModel saved = taxUseCase.create(model);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ApiResult.created(taxMapper.toResponse(saved), msg("tax.created"), location);
    }

    @Operation(summary = "${swagger.tax.get-by-id.summary}", description = "${swagger.tax.get-by-id.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaxResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Tax not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<TaxResponseDto>> findById(
            @Parameter(description = "UUID of the tax to retrieve", required = true)
            @PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        return ApiResult.success(taxMapper.toResponse(model), msg("tax.found"));
    }

    @Operation(summary = "${swagger.tax.get-by-name.summary}", description = "${swagger.tax.get-by-name.desc}")
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResult<TaxResponseDto>> findByName(@PathVariable String name) {
        TaxModel model = taxUseCase.findByName(name);
        return ApiResult.success(taxMapper.toResponse(model), msg("tax.found"));
    }

    @Operation(summary = "${swagger.tax.list.summary}", description = "${swagger.tax.list.desc}")
    @GetMapping
    public ResponseEntity<ApiResult<Page<TaxResponseDto>>> findAll(
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAll(pageable);
        return ApiResult.success(page.map(taxMapper::toResponse), msg("tax.list"));
    }

    @Operation(summary = "${swagger.tax.list-active.summary}", description = "${swagger.tax.list-active.desc}")
    @GetMapping("/active")
    public ResponseEntity<ApiResult<Page<TaxResponseDto>>> findByActive(
            @Parameter(description = "Filter by active status (true/false)", required = true)
            @RequestParam Boolean active,
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findByActive(active, pageable);
        return ApiResult.success(page.map(taxMapper::toResponse), msg("tax.list"));
    }

    @Operation(summary = "${swagger.tax.search.summary}", description = "${swagger.tax.search.desc}")
    @GetMapping("/search")
    public ResponseEntity<ApiResult<Page<TaxResponseDto>>> search(
            @Parameter(description = "Partial name to search for")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active,
            @Valid PageableRequestDto pageableDto) {
        Pageable pageable = pageableUtils.toPageable(pageableDto);
        Page<TaxModel> page = taxUseCase.findAllPaginated(name, active, pageable);
        return ApiResult.success(page.map(taxMapper::toResponse), msg("tax.list"));
    }

    @Operation(summary = "${swagger.tax.update.summary}", description = "${swagger.tax.update.desc}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax updated successfully"),
            @ApiResponse(responseCode = "404", description = "Tax not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<TaxResponseDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaxUpdateDto dto) {
        TaxModel model = taxUseCase.findById(id);
        taxMapper.updateFrom(dto, model);
        TaxModel updated = taxUseCase.save(model);
        return ApiResult.success(taxMapper.toResponse(updated), msg("tax.updated"));
    }

    @Operation(summary = "${swagger.tax.delete.summary}", description = "${swagger.tax.delete.desc}")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable UUID id) {
        taxUseCase.deleteById(id);
        return ApiResult.success(null, msg("tax.deleted"));
    }

    @Operation(summary = "${swagger.tax.activate.summary}", description = "${swagger.tax.activate.desc}")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResult<TaxResponseDto>> activate(@PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        model.setIsActive(true);
        TaxModel updated = taxUseCase.save(model);
        return ApiResult.success(taxMapper.toResponse(updated), msg("tax.activated"));
    }

    @Operation(summary = "${swagger.tax.deactivate.summary}", description = "${swagger.tax.deactivate.desc}")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResult<TaxResponseDto>> deactivate(@PathVariable UUID id) {
        TaxModel model = taxUseCase.findById(id);
        model.setIsActive(false);
        TaxModel updated = taxUseCase.save(model);
        return ApiResult.success(taxMapper.toResponse(updated), msg("tax.deactivated"));
    }
}
