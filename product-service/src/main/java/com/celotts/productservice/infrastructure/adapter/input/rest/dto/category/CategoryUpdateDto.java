package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateDto {
    private String name;
    private String description;
    private Boolean active;
    private String updatedBy;
}