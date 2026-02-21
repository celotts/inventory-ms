package com.celotts.taxservice.infrastructure.adapter.output.postgres.entity.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    // @CreatedBy eliminada
    @Column(name = "created_by", updatable = false, length = 255)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    // @LastModifiedBy eliminada
    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by", length = 255)
    private String deletedBy;

    @Column(name = "deleted_reason", length = 255)
    private String deletedReason;

    @Column(nullable = false)
    private Boolean enabled = Boolean.TRUE;

    @PrePersist
    public void prePersist() {
        if (enabled == null) enabled = Boolean.TRUE;
        if (createdBy == null) createdBy = "system";
    }

    @PreUpdate
    public void preUpdate() {
        // La fecha de actualización la sigue manejando Spring
    }
}
