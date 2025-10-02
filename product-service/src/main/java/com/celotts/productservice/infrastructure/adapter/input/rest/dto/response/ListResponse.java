package com.celotts.productservice.infrastructure.adapter.input.rest.dto.response;

import java.util.List;

public class ListResponse<T> {
    private final List<T> data;
    private final long total;

    private ListResponse(List<T> data) {
        this.data = data;
        this.total = data.size();
    }

    public static <T> ListResponse<T> of(List<T> data) {
        return new ListResponse<>(data);
    }

    public List<T> getData() {
        return data;
    }

    public long getTotal() {
        return total;
    }
}