package com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.supplier;

import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.auditable.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder; // <--- IMPORTANTE
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "supplier", indexes = {
        @Index(name = "idx_supplier_code", columnList = "code"),
        @Index(name = "idx_supplier_email", columnList = "email")
})
@SQLDelete(sql = "UPDATE supplier SET deleted_at = now(), deleted_reason = 'SOFT_DELETE' WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(onlyExplicitlyIncluded = true)
public class SupplierEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Asegúrate de definir la estrategia
    @ToString.Include
    private UUID id;

    @NotBlank(message = "{supplier.code.required}")
    @Column(nullable = false, length = 40, unique = true) // Agregamos UNIQUE
    @ToString.Include
    private String code;

    @NotBlank(message = "{supplier.name.required}")
    @Column(nullable = false, length = 150)
    @ToString.Include
    private String name;

    @Column(name = "tax_id", length = 30)
    private String taxId;

    @Column(name = "email", length = 120)
    private String email;

    @Column(length = 40)
    private String phone;

    @Column(columnDefinition = "text")
    private String address;
}