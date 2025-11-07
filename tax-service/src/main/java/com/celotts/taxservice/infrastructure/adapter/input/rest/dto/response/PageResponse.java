package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * DTO genérico para envolver respuestas paginadas.
 * Transforma objetos Page de Spring Data en una estructura JSON amigable.
 *
 * @param <T> tipo del contenido paginado
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {

    @JsonProperty("data")
    private final List<T> content;

    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    /**
     * Convierte un Page<T> de Spring Data a PageResponse<T>
     */
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

    /**
     * Convierte un Page<S> a PageResponse<T> aplicando un mapper
     * Útil cuando necesitas transformar entities a DTOs
     */
    public static <S, T> PageResponse<T> from(Page<S> page, Function<S, T> mapper) {
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