package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuditableEntity {

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 255)
    private String deletedBy;

    @Column(name = "deleted_reason", length = 255)
    private String deletedReason;

    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = Boolean.TRUE;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = createdAt;
        if (enabled == null) enabled = Boolean.TRUE;
        if (createdBy == null) createdBy = "system";
        if (updatedBy == null) updatedBy = createdBy;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
