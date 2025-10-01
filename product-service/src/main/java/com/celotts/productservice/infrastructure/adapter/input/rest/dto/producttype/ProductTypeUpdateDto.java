package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype;

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
public class ProductTypeUpdateDto {

    @Builder.Default
    private boolean enabled = Boolean.TRUE;

    @Size(max = 100, message = "CreatedBy max length is 100")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "CreatedBy has invalid characters")
    private String updatedBy;
}
