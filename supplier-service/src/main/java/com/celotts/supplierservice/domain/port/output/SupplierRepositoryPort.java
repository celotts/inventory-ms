package com.celotts.supplierservice.domain.port.output;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface SupplierRepositoryPort {

    SupplierModel save(SupplierModel supplier);

    Optional<SupplierModel> findById(UUID id);


    // Unicidad / existencia
    boolean existsById(UUID id);
    boolean existsByName(String name);
    boolean existsByCode(String code);

    void deleteById(UUID id);

    // Consultas con paginación
    Page<SupplierModel> findAll(Pageable pageable);
    Page<SupplierModel> findByNameContaining(String name, Pageable pageable);
    Page<SupplierModel> findByActive(Boolean active);

    Optional<SupplierModel> findByCode(String code);
    // Sugerencias / búsqueda flexible con límite
    List<SupplierModel> findByNameDescription(String query, int limit);

    @Transactional(readOnly = true)
    Optional<SupplierModel> getSupplierById(UUID id);

    @Transactional(readOnly = true)
    Optional<SupplierModel> getSupplierById(Long id);
}