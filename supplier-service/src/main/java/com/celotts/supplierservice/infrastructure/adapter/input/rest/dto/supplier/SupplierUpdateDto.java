package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;

import com.celotts.supplierservice.infrastructure.common.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "DTO for updating an existing supplier")
public class SupplierUpdateDto {

    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "{supplier.code.pattern}",
            groups = ValidationGroups.Update.class
    )
    @Schema(description = "Unique code for the supplier", example = "SUP-001")
    private String code;

    @Size(
            min = 2, max = 150,
            message = "{supplier.name.size}",
            groups = ValidationGroups.Update.class
    )
    @Schema(description = "Name of the supplier", example = "ACME Corporation")
    private String name;

    @Size(
            max = 30,
            message = "{supplier.taxid.size}",
            groups = ValidationGroups.Update.class
    )
    @JsonProperty("tax_id")
    @Schema(description = "Tax identifier (e.g., RFC, VAT number)", example = "XAXX010101000")
    private String taxId;

    @Email(message = "{supplier.email.format}", groups = ValidationGroups.Update.class)
    @Size(
            max = 120,
            message = "{supplier.email.size}",
            groups = ValidationGroups.Update.class
    )
    @Schema(description = "Contact email of the supplier", example = "contact@acme.com")
    private String email;

    @Size(
            max = 40,
            message = "{supplier.phone.size}",
            groups = ValidationGroups.Update.class
    )
    @Schema(description = "Contact phone number", example = "+1-555-123-4567")
    private String phone;

    @Size(
            max = 255,
            message = "{supplier.address.size}",
            groups = ValidationGroups.Update.class
    )
    @Schema(description = "Physical address of the supplier", example = "123 Main St, Anytown, USA")
    private String address;

    @JsonAlias({"active","enabled"})
    @Schema(description = "Whether the supplier is active", example = "true")
    private Boolean enabled;

    // (opcional) garantiza que venga al menos un campo
    @AssertTrue(message = "{supplier.update.at-least-one}")
    @JsonIgnore
    public boolean hasAnyField() {
        return code != null || name != null || taxId != null || email != null ||
                phone != null || address != null || enabled != null;
    }

    public void normalizeFields() {
        this.code    = upper(trimToNull(code));
        this.name    = collapseSpaces(trimToNull(name));
        this.taxId   = upper(trimToNull(taxId));
        this.email   = lower(trimToNull(email));
        this.phone   = collapseSpaces(trimToNull(phone));
        this.address = collapseSpaces(trimToNull(address));
    }

    // --- helpers
    private static String trimToNull(String v){ if (v==null) return null; String t=v.trim(); return t.isEmpty()?null:t; }
    private static String collapseSpaces(String v){ return v==null?null: v.replaceAll("\\s+"," "); }
    private static String upper(String v){ return v==null?null: v.toUpperCase(); }
    private static String lower(String v){ return v==null?null: v.toLowerCase(); }
}
