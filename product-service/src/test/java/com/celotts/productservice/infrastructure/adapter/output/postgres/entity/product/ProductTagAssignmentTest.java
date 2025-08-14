package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTagAssignment;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTagAssignmentTest {

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID());
        ProductTagEntity tag = new ProductTagEntity();
        tag.setId(UUID.randomUUID());
        tag.setName("TAG-001");
        LocalDateTime assignedAt = LocalDateTime.now();

        ProductTagAssignment assignment = new ProductTagAssignment(id, product, tag, assignedAt);

        assertEquals(id, assignment.getId());
        assertEquals(product, assignment.getProduct());
        assertEquals(tag, assignment.getTag());
        assertEquals(assignedAt, assignment.getAssignedAt());
    }

    @Test
    void testSettersAndGetters() {
        ProductTagAssignment assignment = new ProductTagAssignment();

        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID());
        ProductTagEntity tag = new ProductTagEntity();
        tag.setId(UUID.randomUUID());
        tag.setName("T002");
        LocalDateTime assignedAt = LocalDateTime.now();

        assignment.setId(id);
        assignment.setProduct(product);
        assignment.setTag(tag);
        assignment.setAssignedAt(assignedAt);

        assertEquals(id, assignment.getId());
        assertEquals(product, assignment.getProduct());
        assertEquals(tag, assignment.getTag());
        assertEquals(assignedAt, assignment.getAssignedAt());
    }

    @Test
    void testDefaultAssignedAtNotNull() {
        ProductTagAssignment assignment = new ProductTagAssignment();
        assertNotNull(assignment.getAssignedAt());
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID());

        ProductTagEntity tag = new ProductTagEntity();
        tag.setId(UUID.randomUUID());
        tag.setName("TAG");

        LocalDateTime now = LocalDateTime.now();

        ProductTagAssignment a1 = new ProductTagAssignment(id, product, tag, now);
        ProductTagAssignment a2 = new ProductTagAssignment(id, product, tag, now);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        ProductTagAssignment a1 = new ProductTagAssignment();
        ProductTagAssignment a2 = new ProductTagAssignment();
        a2.setId(UUID.randomUUID()); // diferente

        assertNotEquals(a1, a2);
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testEquals_nullAndDifferentClass() {
        ProductTagAssignment assignment = new ProductTagAssignment();
        assertNotEquals(assignment, null);
        assertNotEquals(assignment, "string");
    }

    @Test
    void testEquals_sameInstance() {
        ProductTagAssignment assignment = new ProductTagAssignment();
        assertEquals(assignment, assignment);
    }

    @Test
    void testToString_containsClassNameAndFields() {
        ProductTagAssignment assignment = new ProductTagAssignment();
        assignment.setId(UUID.randomUUID());
        assignment.setAssignedAt(LocalDateTime.now());

        String result = assignment.toString();
        assertNotNull(result);
        assertTrue(result.contains("ProductTagAssignment"));
        assertTrue(result.contains("assignedAt"));
    }

    @Test
    void testCanEqual() {
        ProductTagAssignment assignment1 = new ProductTagAssignment();
        ProductTagAssignment assignment2 = new ProductTagAssignment();

        assertTrue(assignment1.canEqual(assignment2));
        assertFalse(assignment1.canEqual("not assignment"));
    }
}