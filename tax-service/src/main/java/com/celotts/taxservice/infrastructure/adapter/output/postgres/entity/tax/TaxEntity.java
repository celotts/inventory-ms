package com.celotts.taxservice.infrastructure.adapter.output.postgres.entity.tax;

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
import java.util.UUID;

/**
 * Tax Entity - Represents a tax configuration.
 * Extends AuditableEntity for automatic audit fields (createdAt, updatedAt).
 */
@Entity
@Table(name = "tax")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaxEntity extends AuditableEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "{tax.code.required}")
    @Size(max = 30, message = "{tax.code.max-length}")
    @Column(name = "code", nullable = false, length = 30, unique = true)
    private String code;

    @NotBlank(message = "{tax.name.required}")
    @Size(min = 2, max = 120, message = "{tax.name.size}")
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @NotBlank(message = "{tax.description.required}")
    @Size(min = 2, max = 120, message = "{tax.description.size}")
    @Column(name = "description", nullable = false, length = 120)
    private String description;

    @NotNull(message = "{tax.rate.required}")
    @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
    @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}")
    @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}")
    @Column(name = "rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    @NotNull(message = "{tax.validfrom.required}")
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @NotNull(message = "{tax.is.active.required}")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /**
     * Custom constructor with all fields and validation annotations.
     *
     * @param id Tax identifier (auto-generated)
     * @param code Tax code (must be unique)
     * @param name Tax name
     * @param rate Tax rate (0-100%)
     * @param validFrom Start date of validity
     * @param validTo End date of validity (optional)
     * @param isActive Whether tax is active
     */
    public TaxEntity(
            UUID id,
            @NotBlank(message = "{tax.code.required}")
            @Size(max = 30, message = "{tax.code.max-length}")
            String code,
            @NotBlank(message = "{tax.name.required}")
            @Size(min = 2, max = 120, message = "{tax.name.size}")
            String name,
            @NotBlank(message = "{tax.description.required}")
            @Size(min = 2, max = 120, message = "{tax.description.size}")
            String description,
            @NotNull(message = "{tax.rate.required}")
            @DecimalMin(value = "0.00", inclusive = true, message = "{tax.rate.min}")
            @DecimalMax(value = "100.00", inclusive = true, message = "{tax.rate.max}")
            @Digits(integer = 3, fraction = 2, message = "{tax.rate.digits}")
            BigDecimal rate,
            @NotNull(message = "{tax.validfrom.required}")
            LocalDate validFrom,
            LocalDate validTo,
            @NotNull(message = "{tax.is.active.required}")
            Boolean isActive
    ) {
        super();
        this.id = id;
        this.code = code;
        this.name = name;
        this.rate = rate;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.isActive = isActive;
    }
}