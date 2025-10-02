package com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot;

public record LotDisposeRequestDto(
        String reference,
        String reason,
        String user
) {}
