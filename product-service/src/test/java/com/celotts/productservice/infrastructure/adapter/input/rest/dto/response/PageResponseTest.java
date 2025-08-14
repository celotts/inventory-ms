package com.celotts.productservice.infrastructure.adapter.input.rest.dto.response;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.response.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    private Page<String> mockPage;
    private List<String> content;

    @BeforeEach
    void setUp() {
        content = List.of("Item1", "Item2", "Item3");
        mockPage = new PageImpl<>(content, PageRequest.of(2, 3), 10); // Página 2, tamaño 3, total 10 elementos
    }

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        PageResponse<String> response = new PageResponse<>(mockPage);

        assertEquals(content, response.getContent());
        assertEquals(2, response.getPageNumber());
        assertEquals(3, response.getPageSize());
        assertEquals(10, response.getTotalElements());
        assertEquals(4, response.getTotalPages()); // 10 elementos / tamaño 3 = 4 páginas
        assertFalse(response.isLast()); // Página 2 de 4 no es la última
    }

    @Test
    void constructor_shouldHandleEmptyPage() {
        Page<String> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 5), 0);
        PageResponse<String> response = new PageResponse<>(emptyPage);

        assertNotNull(response.getContent());
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getPageNumber());
        assertEquals(5, response.getPageSize());
        assertEquals(0, response.getTotalElements());
        assertEquals(0, response.getTotalPages());
        assertTrue(response.isLast());
    }
}