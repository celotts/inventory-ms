package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> {
    private final List<T> data;
    private final long total;

    private ListResponse(List<T> data) {
        this.data = data;
        this.total = data.size();

    }

    public static <T> ListResponse<T> of(List<T> data) {
        return  new ListResponse<>(data);
    }
}
