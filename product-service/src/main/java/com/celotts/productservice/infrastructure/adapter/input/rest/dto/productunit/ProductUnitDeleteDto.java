package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductUnitDeleteDto {

    @NotNull(message = "ProductUnit ID must not be null")
    UUID id;

    @Size(max = 100, message = "deletedBy max length is 100")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "deletedBy has invalid characters")
    String deletedBy;

    @Size(max = 255, message = "Reason must not exceed 255 characters")
    String reason;
}