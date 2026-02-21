package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productimage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageUpdateDto {

    @NotBlank(message = "{validation.product-image.update.url.not-blank}")
    @Size(max = 2048, message = "{validation.product-image.update.url.size}")
    String url;

    Boolean enabled;

    @PastOrPresent(message = "{validation.product-image.update.uploaded-at.past-or-present}")
    LocalDateTime uploadedAt;

    @Size(max = 100, message = "{validation.product-image.update.updated-by.size}")
    String updatedBy;
}