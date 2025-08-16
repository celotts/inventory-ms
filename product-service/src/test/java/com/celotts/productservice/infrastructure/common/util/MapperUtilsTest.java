package com.celotts.productservice.infrastructure.common.util;

import com.celotts.productserviceOld.infrastructure.common.util.MapperUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class MapperUtilsTest {

    @Test
    void updateFieldIfNotNull_shouldUpdateValueWhenNotNull() {
        AtomicReference<String> field = new AtomicReference<>("oldValue");

        MapperUtils.updateFieldIfNotNull("newValue", field::set);

        assertEquals("newValue", field.get());
    }

    @Test
    void updateFieldIfNotNull_shouldNotUpdateValueWhenNull() {
        AtomicReference<String> field = new AtomicReference<>("original");

        // No debe actualizar el campo si el valor es null
        MapperUtils.updateFieldIfNotNull(null, field::set);

        assertEquals("original", field.get()); // Se espera que no cambie
    }
}