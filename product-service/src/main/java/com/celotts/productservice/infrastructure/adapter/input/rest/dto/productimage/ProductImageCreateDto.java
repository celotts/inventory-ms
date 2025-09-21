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

    @NotNull(message = "Product ID is required")
    UUID productId;

    @NotBlank(message = "Url is required")
    @Size(max = 2048, message = "url max length is 2048")
    String url;

    Boolean enabled;

    @PastOrPresent(message = "UploadedAt must be in the past or present")
    LocalDateTime uploadedAt;

    @Size(max = 100, message = "CreatedBy max length is 100")
    String createdBy;
}