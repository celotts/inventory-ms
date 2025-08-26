package com.celotts.productservice.infrastructure.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequestDto {

    @Builder.Default
    @PositiveOrZero(message = "page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "size must be >= 1")
    private Integer size = 20;

    @Builder.Default
    @Pattern(regexp = "(?i)ASC|DESC", message = "sortDir must be 'ASC' or 'DESC'")
    private String sortDir = "DESC";

    @Builder.Default
    private String sortBy = "createdAt";
}