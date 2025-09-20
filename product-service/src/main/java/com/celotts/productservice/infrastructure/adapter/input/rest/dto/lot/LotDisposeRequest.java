package com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot;

public record LotDisposeRequest(
        String reference,
        String reason,
        String user
) {}
