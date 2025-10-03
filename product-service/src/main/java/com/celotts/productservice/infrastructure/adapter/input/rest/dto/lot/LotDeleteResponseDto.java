package com.celotts.productservice.infrastructure.adapter.input.rest.dto.lot;

import java.time.Instant;
import java.util.UUID;

public record LotDeleteResponseDto(
        UUID lotId,
        String reference,
        String reason,
        String user,
        Instant deletedAt
) {}
