package com.celotts.productservice.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryModel {
    UUID id;
    UUID productId;
    UUID categoryId;
    LocalDateTime assignedAt;
    Boolean enabled;
    LocalDateTime createdAt;
    String createdBy;
    Boolean active;
    String updatedBy;
    LocalDateTime updatedAt;


}
