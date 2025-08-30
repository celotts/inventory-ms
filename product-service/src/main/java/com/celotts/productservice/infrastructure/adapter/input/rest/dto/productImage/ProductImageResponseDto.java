package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponseDto {
    private UUID id;
    private UUID productId;
    private String url;
    private String createdBy;
    private String updatedBy;
    private Boolean enabled;
    private LocalDateTime uploadedAt;
    private LocalDateTime updateAt;
    private LocalDateTime createdAt;
}
