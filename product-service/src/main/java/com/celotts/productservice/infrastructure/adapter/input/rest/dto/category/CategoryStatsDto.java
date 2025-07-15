package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para estadísticas de categorías
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryStatsDto {

    private long totalCategories;
    private long activeCategories;
    private long inactiveCategories;
    private long deletedCategories;

    private double activePercentage;
    private double inactivePercentage;
    private double deletedPercentage;

}