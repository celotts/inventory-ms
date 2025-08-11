package com.celotts.productservice.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeModel {

    private UUID id;
    private String code;
    private String name;
    private String descpiption;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updateBy;

}
