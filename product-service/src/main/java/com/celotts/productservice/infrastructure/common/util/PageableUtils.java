package com.celotts.productservice.infrastructure.common.util;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    public static Pageable toPageable(PageableRequestDto req) {
        return PageRequest.of(
                req.getPage(),
                req.getSize(),
                Sort.by(Sort.Direction.fromString(req.getSortDir()), req.getSortBy())
        );
    }
}