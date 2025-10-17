package com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.supplier;

import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.auditable.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(
        name = "supplier",
        indexes = {
                @Index(name = "idx_supplier_code", columnList = "code"),
                @Index(name = "idx_supplier_email", columnList = "email")
        }
)
@SQLDelete(sql = "UPDATE supplier SET deleted_at = now(), deleted_reason = 'SOFT_DELETE' WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class SupplierEntity extends AuditableEntity {

    @Id
    @GeneratedValue
    @ToString.Include
    private UUID id;

    @NotBlank(message = "Supplier code is required")
    @Size(min = 3, max = 40, message = "Supplier code must be 3 to 40 characters")
    @Pattern(regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "Code must be uppercase letters, digits, '-' or '_'")
    @Column(nullable = false, length = 40)
    @ToString.Include
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 150, message = "Name must be 2 to 150 characters")
    @Column(nullable = false, length = 150)
    @ToString.Include
    private String name;

    @Size(max = 30, message = "Tax ID must not exceed 30 characters")
    @Column(name = "tax_id", length = 30)
    private String taxId;

    @Email(message = "Invalid email format")
    @Size(max = 120, message = "The email must not exceed 120 characters")
    @Column(name = "email", length = 120)
    private String email;

    @Size(max = 40, message = "Phone must not exceed 40 characters")
    @Column(length = 40)
    private String phone;

    @Column(columnDefinition = "text")
    private String address;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = Boolean.TRUE;
}