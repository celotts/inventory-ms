package com.celotts.supplierservice.application.usecase;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SupplierUseCase {

    // Commands
    SupplierModel create(SupplierModel supplier);
    SupplierModel update(UUID id, SupplierModel partial);
    void delete(UUID id);

    // Sobrecarga para metadata de borrado (por ahora no-op)
    default void delete(UUID id, String deletedBy, String reason) {
        delete(id);
    }

    // Queries
    SupplierModel getById(UUID id);
    Page<SupplierModel> findAll(Pageable pageable);
    Page<SupplierModel> findByNameContaining(String q, Pageable pageable);
    Page<SupplierModel> findByActive(Boolean active);
    List<SupplierModel> searchByNameDescription(String q, int limit);
    boolean existsByName(String name);
}