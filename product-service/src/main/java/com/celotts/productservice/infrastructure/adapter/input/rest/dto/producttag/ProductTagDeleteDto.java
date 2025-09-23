package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttag;

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
public class ProductTagDeleteDto {

    @NotNull(message = "ID is required")
    UUID id;

    @Size(max = 100, message = "DeletedBy max length is 100")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "DeletedBy has invalid characters")
    String deletedBy;

    @Size(max = 255, message = "Reason must not exceed 255 characters")
    String reason;
}