package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDeleteDto {
    @NotNull(message = "Category ID is required")
    private UUID id;
}

