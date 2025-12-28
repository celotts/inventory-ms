package com.celotts.purchaseservice.infrastructure.common.util;

import com.celotts.purchaseservice.infrastructure.common.dto.PageableRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableUtils {
    public static Pageable toPageable(PageableRequestDto req) {
        // Usa los métodos helper que ya creaste en el DTO
        return PageRequest.of(
                req.getPageOrDefault(),
                req.getSizeOrDefault(),
                req.toSort()
        );
    }
}