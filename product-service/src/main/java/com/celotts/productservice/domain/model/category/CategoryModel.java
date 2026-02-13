package com.celotts.productservice.domain.model.category;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private UUID id;
    private String name;
    private String description;
    private Boolean enabled; // Re-añadido
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
