package com.celotts.productservice.domain.model.common;

import java.time.Instant;

public record AuditLite(
        Instant createdAt,
        String createdBy,
        Instant deletedAt,
        String deletedBy
) {}