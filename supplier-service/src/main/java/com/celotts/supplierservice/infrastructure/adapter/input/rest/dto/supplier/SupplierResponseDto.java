package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a supplier response")
public class SupplierResponseDto {

    @Schema(description = "Unique identifier of the supplier", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(description = "Unique code for the supplier", example = "SUP-001")
    private String code;

    @Schema(description = "Name of the supplier", example = "ACME Corporation")
    private String name;

    @JsonProperty("tax_id")
    @Schema(description = "Tax identifier (e.g., RFC, VAT number)", example = "XAXX010101000")
    private String taxId;

    @Schema(description = "Contact email of the supplier", example = "contact@acme.com")
    private String email;

    @Schema(description = "Contact phone number", example = "+1-555-123-4567")
    private String phone;

    @Schema(description = "Physical address of the supplier", example = "123 Main St, Anytown, USA")
    private String address;

    @JsonProperty("active")
    @Schema(description = "Whether the supplier is active", example = "true")
    private Boolean enabled;

    @Schema(description = "User who created the supplier record", example = "admin")
    private String createdBy;

    @Schema(description = "User who last updated the supplier record", example = "admin")
    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the record was created", example = "2024-11-20T14:00:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the record was last updated", example = "2024-11-21T09:15:00")
    private LocalDateTime updatedAt;
}
