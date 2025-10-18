package com.celotts.supplierservice.domain.port.output.supplier;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface SupplierRepositoryPort {

    SupplierModel save(SupplierModel supplier);

    Optional<SupplierModel> findById(UUID id);
    Optional<SupplierModel> findByName(String name);

    // ⬇️ NUEVOS (por code)
    Optional<SupplierModel> findByCode(String code);
    boolean existsByCode(String code);

    List<SupplierModel> findAll();
    List<SupplierModel> findAllById(List<UUID> ids);
    List<SupplierModel> findByNameContaining(String name);
    List<SupplierModel> findByNameDescription(String query, int limit);

    void deleteById(UUID id);

    boolean existsById(UUID id);
    boolean existsByName(String name);

    Page<SupplierModel> findAll(Pageable pageable);
    Page<SupplierModel> findByNameContaining(String name, Pageable pageable);
    Page<SupplierModel> findByActive(Boolean active);
}