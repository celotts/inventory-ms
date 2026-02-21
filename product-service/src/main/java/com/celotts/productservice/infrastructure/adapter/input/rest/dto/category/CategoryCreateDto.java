package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryCreateDto {
    @NotBlank(message = "{validation.category.create.name.not-blank}")
    @Size(max = 100, message = "{validation.category.create.name.size}")
    private String name;

    @NotBlank(message = "{validation.category.create.description.not-blank}")
    @Size(max = 500, message = "{validation.category.create.description.size}")
    private String description;

    @Size(max = 100, message = "{validation.category.create.created-by.size}")
    @NotBlank(message = "{validation.category.create.created-by.not-blank}")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "{validation.category.create.created-by.pattern}")
    private String createdBy;

    private String updatedBy;
}