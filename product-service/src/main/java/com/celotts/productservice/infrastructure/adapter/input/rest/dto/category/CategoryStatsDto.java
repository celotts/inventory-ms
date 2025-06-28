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

    /**
     * Calcula el porcentaje de categorías activas
     */
    //TODO: NO SE USA
    public double getActivePercentage() {
        if (totalCategories == 0) {
            return 0.0;
        }
        return (double) activeCategories / totalCategories * 100;
    }

    /**
     * Calcula el porcentaje de categorías inactivas
     */
    //TODO: NO SE USA
    public double getInactivePercentage() {
        if (totalCategories == 0) {
            return 0.0;
        }
        return (double) inactiveCategories / totalCategories * 100;
    }

    /**
     * Calcula el porcentaje de categorías eliminadas
     */
    //TODO: NO SE USA
    public double getDeletedPercentage() {
        if (totalCategories == 0) {
            return 0.0;
        }
        return (double) deletedCategories / totalCategories * 100;
    }
}