package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttagassignment;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductTagAssignmentDeleteDto {

    @NotNull(message = "ID is required")
    UUID id;

    @Size(max = 100, message = "updatedBy max length is 100")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "updatedBy has invalid characters")
    String updatedBy;

    @PastOrPresent(message = "updatedAt must be in the past or present")
    LocalDateTime updatedAt;

    @Size(max = 100, message = "deletedBy max length is 100")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "deletedBy has invalid characters")
    String deletedBy;

    @Size(max = 255, message = "Reason must not exceed 255 characters")
    String reason;
}