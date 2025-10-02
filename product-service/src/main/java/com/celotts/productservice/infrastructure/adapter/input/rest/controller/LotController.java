package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.views.LotAvailableView;
import com.celotts.productservice.domain.model.views.LotExpiredView;
import com.celotts.productservice.domain.port.input.inventory.LotUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.PageResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.lot.LotDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotUseCase lotUseCase;
    private final LotDtoMapper mapper;

    @PostMapping
    public ResponseEntity<LotResponseDto> create(@Valid @RequestBody LotCreateDto dto) {
        LotModel created = lotUseCase.create(mapper.toModel(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<PageResponse<LotResponseDto>> listByProduct(@RequestParam UUID productId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "20") int size) {
        var paged = lotUseCase.listByProduct(productId, PageRequest.of(page, size));
        return ResponseEntity.ok(PageResponse.from(paged, mapper::toResponse));
    }

    @PostMapping("/{id}/dispose")
    public ResponseEntity<LotDeleteResponseDto> dispose(@PathVariable UUID id,
                                                        @RequestBody LotDisposeRequestDto req) {
        Instant deletedAt = lotUseCase.dispose(id, req.reference(), req.reason(), req.user());
        return ResponseEntity.ok(new LotDeleteResponseDto(
                id, req.reference(), req.reason(), req.user(), deletedAt
        ));
    }

    @GetMapping("/available")
    public ResponseEntity<PageResponse<LotAvailableView>> available(@RequestParam UUID productId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size) {
        var paged = lotUseCase.listLotsAvailable(productId, PageRequest.of(page, size));
        // Si luego creas LotAvailableDto, reemplaza el tipo y mapea aquí.
        return ResponseEntity.ok(PageResponse.from(paged));
    }

    @GetMapping("/expired")
    public ResponseEntity<PageResponse<LotExpiredView>> expired(@RequestParam UUID productId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "20") int size) {
        var paged = lotUseCase.listLotsExpired(productId, PageRequest.of(page, size));
        return ResponseEntity.ok(PageResponse.from(paged));
    }

    @PostMapping("/mark-expired-and-list")
    public ResponseEntity<PageResponse<LotResponseDto>> markExpiredAndList(@RequestParam(defaultValue = "7") int days,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "20") int size) {
        var paged = lotUseCase.markExpiredAndList(days, PageRequest.of(page, size));
        return ResponseEntity.ok(PageResponse.from(paged, mapper::toResponse));
    }
}