package com.celotts.productservice.domain.model;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductUnitModel {

    private UUID id;
    private String code;          //  ← NUEVO
    private String name;
    private String description;
    private String symbol;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

}
