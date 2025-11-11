package com.celotts.taxservice.infrastructure.adapter.output.postgres.entity.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime; // <-- CAMBIO: Usar OffsetDateTime para zona horaria

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    // Spring Data JPA Auditing se encargará de inicializar estos campos
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 255)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    // Campos de Soft Delete
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by", length = 255)
    private String deletedBy;

    @Column(name = "deleted_reason", length = 255)
    private String deletedReason;

    @Column(nullable = false)
    private Boolean enabled = Boolean.TRUE;

    // MANTENEMOS solo la lógica que Spring Data Auditing NO maneja.
    @PrePersist
    public void prePersist() {
        // La inicialización de createdAt y updatedAt se ELIMINA, ya que la maneja @CreatedDate/@LastModifiedDate
        if (enabled == null) enabled = Boolean.TRUE;
    }

    // ELIMINAMOS @PreUpdate. Spring Data Auditing lo maneja con @LastModifiedDate.
}