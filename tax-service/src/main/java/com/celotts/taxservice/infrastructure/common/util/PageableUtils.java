package com.celotts.taxservice.infrastructure.common.util;

import com.celotts.taxservice.infrastructure.common.dto.PageableRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableUtils {

    public Pageable toPageable(PageableRequestDto req) {
        if (req == null) {
            return PageRequest.of(0, 20, Sort.by("createdAt").descending());
        }

        int page = req.getPageOrDefault();
        int size = req.getSizeOrDefault();
        Sort sort = req.toSort();

        return PageRequest.of(page, size, sort);
    }

    public Pageable toPageableWithSort(PageableRequestDto req, String sortBy, String sortDir) {
        if (req == null) {
            req = PageableRequestDto.builder().build();
        }

        int page = req.getPageOrDefault();
        int size = req.getSizeOrDefault();

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(page, size, sort);
    }

    public Pageable toPageableWithDefaults(Integer page, Integer size, String sortBy, String sortDir) {
        int pageNum = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 ? size : 20;
        String field = sortBy != null ? sortBy : "createdAt";
        String direction = sortDir != null ? sortDir : "DESC";

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        return PageRequest.of(pageNum, pageSize, sort);
    }
}