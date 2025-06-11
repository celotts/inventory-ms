package com.celotts.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBrandModel {

    private UUID id;
    private String name;
    private String description;
    private boolean enabled;
    private String createdBy;
    private String updatedBy;

}
