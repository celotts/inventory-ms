package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTagAssignment;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagAssignmentUpdateDto {
    // Opcional: si no viene, en el servicio puedes asumir TRUE
    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    @Size(max = 100, message = "createdBy max length is 100")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "createdBy has invalid characters")
    private String updatedBy;
}
