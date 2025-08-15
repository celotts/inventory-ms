package com.celotts.productservice.infrastructure.common.jpa;

import java.time.LocalDateTime;

/** Marcador temporal para compilar. Implementa luego según tu auditoría real. */
// com.celotts.productservice.infrastructure.common.jpa.Auditable
public interface Auditable {
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
    void setCreatedBy(String createdBy);
    void setUpdatedBy(String updatedBy);

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    String getCreatedBy();
    String getUpdatedBy();
}