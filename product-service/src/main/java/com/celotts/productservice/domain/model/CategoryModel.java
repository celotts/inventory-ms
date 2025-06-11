package com.celotts.productservice.domain.model;

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
public class CategoryModel {
    private UUID id;
    private String name;
    private String description;
    private Boolean active;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String name, String description, Boolean active, String updatedBy) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
}