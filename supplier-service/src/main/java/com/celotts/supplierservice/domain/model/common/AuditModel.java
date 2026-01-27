package com.celotts.supplierservice.domain.model.common;

import java.time.Instant;

public record AuditModel(
        Instant createdAt,
        String createdBy,
        Instant updatedAt,
        String updatedBy,
        Instant deletedAt,
        String deletedBy,
        String deletedReason
) {}