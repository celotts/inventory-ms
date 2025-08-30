package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage;

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

    @NotBlank(message = "Url is required")
    @Size(max = 2048, message = "url max length is 2048")
    String url;

    Boolean enabled;

    @PastOrPresent(message = "UploadedAt must be in the past or present")
    LocalDateTime uploadedAt;

    @Size(max = 100, message = "updatedBy max length is 100")
    String updatedBy;
}
