package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDeleteDto {

    @NotNull(message = "Category ID must not be null")
    UUID id;
}