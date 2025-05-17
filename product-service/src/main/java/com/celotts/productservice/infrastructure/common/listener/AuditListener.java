package com.celotts.productservice.infrastructure.common.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AuditListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Auditable auditable) {
            LocalDateTime now = LocalDateTime.now();
            auditable.setCreatedAt(now);
            auditable.setCreatedBy(now.toString());
            auditable.setUpdatedAt(null);
            auditable.setUpdatedBy(null);

        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof Auditable auditable) {
            LocalDateTime now = LocalDateTime.now();
            auditable.setUpdatedAt(now);
            auditable.setUpdatedBy(now.toString());
        }
    }
}
