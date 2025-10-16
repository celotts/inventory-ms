package com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity;

import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.auditable.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="supplier")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierEntity  extends AuditableEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Supplier code  is required")
    @Column(nullable = false, length = 100)
    private String code;

    @NotBlank(message = "Tax ID must not exceed 30 characters")
    @Column(nullable = false, length = 30)
    private String tax_id;
}
 /*
    CREATE TABLE supplier (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(40) NOT NULL CHECK (code ~ '^[A-Z0-9\-_]{3,40}$'),
    name VARCHAR(150) NOT NULL CHECK (length(trim(name)) >= 2),
    tax_id VARCHAR(30),                -- RFC u otro identificador
    email VARCHAR(120),
    phone VARCHAR(40),
    address TEXT,
    enabled BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
     */