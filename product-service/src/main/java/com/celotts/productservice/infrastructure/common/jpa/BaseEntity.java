package com.celotts.productservice.infrastructure.common.jpa;

import com.celotts.productservice.infrastructure.common.listener.AuditListener; // <- ESTE import
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditListener.class) // usa el listener del paquete .listener
@Getter
@Setter
public abstract class BaseEntity implements Auditable {
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    protected String createdBy;

    @Column(name = "updated_by", length = 100)
    protected String updatedBy;

    @Override public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    @Override public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    @Override public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    @Override public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    @Override public LocalDateTime getCreatedAt() { return createdAt; }
    @Override public LocalDateTime getUpdatedAt() { return updatedAt; }
    @Override public String getCreatedBy() { return createdBy; }
    @Override public String getUpdatedBy() { return updatedBy; }
}