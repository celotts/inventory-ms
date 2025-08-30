package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductIamgeDeleteDto {
    @NotBlank(message = "Product image id is required")
    @NotNull(message = "Product image ID is not Null")
    private UUID id;
}
