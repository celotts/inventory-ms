package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productimage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductImageCreateDto {

    @NotNull(message = "{validation.product-image.create.product-id.not-null}")
    UUID productId;

    @NotBlank(message = "{validation.product-image.create.url.not-blank}")
    @Size(max = 2048, message = "{validation.product-image.create.url.size}")
    String url;

    Boolean enabled;

    @PastOrPresent(message = "{validation.product-image.create.uploaded-at.past-or-present}")
    LocalDateTime uploadedAt;

    @Size(max = 100, message = "{validation.product-image.create.created-by.size}")
    String createdBy;
}