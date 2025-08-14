package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUnitDeleteDto {
    @NotBlank(message = "Product brand ID is required")
    @NotNull(message =  "Product Brand ID is not Null")
    private UUID id;
}
