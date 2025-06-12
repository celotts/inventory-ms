package com.celotts.productservice.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    @Id
    private UUID id;
    private String name;
    private String description;
    private Boolean active;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private boolean deleted = false;

    public void update(String name, String description, Boolean active, String updatedBy) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }

}