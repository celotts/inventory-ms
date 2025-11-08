package com.celotts.taxservice.infrastructure.adapter.output.postgres.entity;

import com.celotts.taxservice.infrastructure.adapter.output.postgres.entity.shared.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tax")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaxEntity extends AuditableEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 30, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public TaxEntity(UUID id, @NotBlank(message = "{tax.code.required}") @Size(max = 40, message = "{tax.code.max-length}") String code, @NotBlank(message = "{tax.name.required}") @Size(min = 2, max = 120, message = "{tax.name.size}") String name, @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}") @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}") @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}") @NotNull(message = "{tax.rate.required}") BigDecimal rate, LocalDate validFrom, LocalDate validTo, Boolean isActive, LocalDateTime localDateTime, LocalDateTime localDateTime1) {
        super();
    }
}