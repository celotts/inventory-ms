package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDeleteDto {

    @NotBlank(message = "Category ID is required")
    @NotNull(message = "Category ID is nto null")
    private UUID id;
}