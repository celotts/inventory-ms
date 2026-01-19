package com.celotts.purchaseservice.infrastructure.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequestDto {

    @Builder.Default
    @PositiveOrZero(message = "{pageable.page.min}")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "{pageable.size.min}")
    private Integer size = 20;

    @Builder.Default
    @Pattern(regexp = "(?i)ASC|DESC", message = "{pageable.sortDir.invalid}")
    private String sortDir = "DESC";

    @Builder.Default
    private String sortBy = "createdAt";

    // --- Helpers ---
    public int getPageOrDefault() {
        return page != null && page >= 0 ? page : 0;
    }

    public int getSizeOrDefault() {
        if (size == null) return 20;
        return Math.max(1, size);
    }

    public Sort toSort() {
        String direction = sortDir != null ? sortDir : "DESC";
        String field = sortBy != null ? sortBy : "createdAt";

        return direction.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();
    }
}