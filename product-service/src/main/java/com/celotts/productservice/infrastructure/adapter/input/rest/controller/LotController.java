package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.views.LotAvailableView;
import com.celotts.productservice.domain.model.views.LotExpiredView;
import com.celotts.productservice.domain.port.input.inventory.LotUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotDeleteResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotDisposeRequestDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot.LotResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.lot.LotDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.CREATED)
    public LotResponseDto create(@Valid @RequestBody LotCreateDto dto) {
        LotModel created = lotUseCase.create(mapper.toModel(dto));
        return mapper.toResponse(created);
    }

    @GetMapping
    public Page<LotResponseDto> listByProduct(@RequestParam UUID productId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        return lotUseCase
                .listByProduct(productId, PageRequest.of(page, size))
                .map(mapper::toResponse);
    }

    @PostMapping("/{id}/dispose")
    public ResponseEntity<LotDeleteResponseDto> dispose(@PathVariable UUID id,
                                                        @RequestBody LotDisposeRequestDto req) {
        Instant deletedAt = lotUseCase.dispose(id, req.reference(), req.reason(), req.user());

        return ResponseEntity.ok(new LotDeleteResponseDto(
                id,
                req.reference(),
                req.reason(),
                req.user(),
                deletedAt
        ));
    }

    @GetMapping("/available")
    public Page<LotAvailableView> available(@RequestParam UUID productId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return lotUseCase.listLotsAvailable(productId, PageRequest.of(page, size));
    }

    @GetMapping("/expired")
    public Page<LotExpiredView> expired(@RequestParam UUID productId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        return lotUseCase.listLotsExpired(productId, PageRequest.of(page, size));
    }

    @PostMapping("/mark-expired-and-list")
    public Page<LotResponseDto> markExpiredAndList(@RequestParam(defaultValue = "7") int days,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        return lotUseCase.markExpiredAndList(days, PageRequest.of(page, size))
                .map(mapper::toResponse);
    }
}