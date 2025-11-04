package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
public class PageResponse<T> {

    @JsonProperty("data")
    private final List<T> content;

    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private int totalPages;
    private final boolean last;

    private PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize=pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()

        );
    }

    public static  <S, T> PageResponse<T> from(Page<S> page, Function<S, T> mapper) {
        List<T> data = page.map(mapper).getContent();
        return new PageResponse<>(
                data,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
