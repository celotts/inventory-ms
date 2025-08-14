package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDeleteDto {

    @NotNull(message = "Category ID must not be null")
    private UUID id;
}