package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@lombok.experimental.SuperBuilder
public class ProductCategoryRequestDto extends PageableRequestDto {

    private UUID productId;
    private UUID categoryId;
    private Boolean enabled;

    private String updatedBy;
    private String createdBy;

    // Rangos de fechas
    private LocalDateTime assignedFrom;
    private LocalDateTime assignedTo;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private LocalDateTime updatedFrom;
    private LocalDateTime updatedTo;
}