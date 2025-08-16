package com.celotts.productservice.infrastructure.common.entity;

import com.celotts.productserviceOld.infrastructure.common.entity.BaseEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class BaseEntityTest {

    // Clase hija simulada para testear BaseEntity
    static class DummyEntity extends BaseEntity {}

    @Test
    void testSettersAndGetters() {
        DummyEntity entity = new DummyEntity();

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now.plusHours(1));
        entity.setCreatedBy("admin");
        entity.setUpdatedBy("editor");

        assertEquals(now, entity.getCreatedAt());
        assertEquals(now.plusHours(1), entity.getUpdatedAt());
        assertEquals("admin", entity.getCreatedBy());
        assertEquals("editor", entity.getUpdatedBy());
    }
}