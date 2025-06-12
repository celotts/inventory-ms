package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDeleteDto {

    @NotNull(message = "Category ID is required")
    private UUID id;
}