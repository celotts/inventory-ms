package com.celotts.supplierservice.domain.port.input;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SupplierUseCase {
    SupplierModel create(SupplierModel supplier);
    SupplierModel update(UUID id, SupplierModel supplier);
    void delete(UUID id);
    void delete(UUID id, String deletedBy, String reason);

    SupplierModel getById(UUID id);
    SupplierModel getByCode(String code);
    boolean existsByCode(String code);

    Page<SupplierModel> findAll(Pageable pageable);
    Page<SupplierModel> findByNameContaining(String name, Pageable pageable);
    Page<SupplierModel> findByActive(Boolean active);

    List<SupplierModel> searchByNameDescription(String q, int limit);
    boolean existsByName(String name);

    // Este método es redundante con getById, lo eliminamos para evitar duplicados.
    // Si se necesita con otro nombre, debe tener un propósito diferente.
    // Optional<SupplierModel> getSupplierById(UUID id);

    boolean existsById(UUID id);
}
