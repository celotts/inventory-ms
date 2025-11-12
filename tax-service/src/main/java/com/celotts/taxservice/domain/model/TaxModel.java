package com.celotts.taxservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tax")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaxModel {

    @Id
    @Builder.Default // <-- Lombok: Establece el valor por defecto al usar el builder
    // Se elimina @GeneratedValue. El control pasa a la aplicación (Java).
    private UUID id = UUID.randomUUID();

    @Column(length = 40, nullable = false, unique = true)
    @NotBlank(message = "{tax.code.required}")
    @Size(max = 40, message = "{tax.code.max-length}")
    private String code;

    @Column(length = 120, nullable = false)
    @NotBlank(message = "{tax.name.required}")
    @Size(min = 2, max = 120, message = "{tax.name.size}")
    private String name;

    @Column(name = "rate", precision = 5, scale = 2, nullable = false)
    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}")
    @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}")
    @NotNull(message = "{tax.rate.required}")
    private BigDecimal rate;

    @Column(name = "valid_from", nullable = false)
    @Builder.Default
    private LocalDate validFrom = LocalDate.now();

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // Auditoría
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // Quién creó/modificó (opcional, útil para trazabilidad)
    @Column(name = "created_by", length = 120)
    private String createdBy;

    @Column(name = "updated_by", length = 120)
    private String updatedBy;

    // Métodos de auditoría (se mantienen por ahora, pero se sugiere moverlos)
    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        // Si el ID es null (porque no se usó el builder, sino el constructor default), se genera aquí
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}