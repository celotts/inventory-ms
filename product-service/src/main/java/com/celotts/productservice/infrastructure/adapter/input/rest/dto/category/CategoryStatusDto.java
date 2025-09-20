package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryStatusDto {

    private long totalCategories;
    private long activeCategories;
    private long inactiveCategories;
    private long deletedCategories;

    private double activePercentage;
    private double inactivePercentage;
    private double deletedPercentage;

}