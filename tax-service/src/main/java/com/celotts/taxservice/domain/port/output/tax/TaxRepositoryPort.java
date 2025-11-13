package com.celotts.taxservice.domain.port.output.tax;

import com.celotts.taxservice.domain.model.tax.TaxModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaxRepositoryPort {

    // ========== CRUD Básico ==========
    TaxModel save(TaxModel tax);

    Optional<TaxModel> findById(UUID id);

    void deleteById(UUID id);

    // ========== Búsquedas por campo ==========
    Optional<TaxModel> findByName(String name);

    Optional<TaxModel> findByCode(String code);

    List<TaxModel> findAll();

    List<TaxModel> findById(List<UUID> ids);

    // ========== Búsquedas contains ==========
    List<TaxModel> findByNameContaining(String name);

    List<TaxModel> findByNameOrDescription(String query, int limit);

    // ========== Existencias ==========
    boolean existsById(UUID id);

    boolean existsByName(String name);

    boolean existsByCode(String code);

    // ========== Paginación ==========
    Page<TaxModel> findAll(Pageable pageable);

    Page<TaxModel> findByNameContaining(String name, Pageable pageable);

    Page<TaxModel> findByActive(Boolean active, Pageable pageable);

    Page<TaxModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);

    Page<TaxModel> findAllPaginated(String name, Boolean active, Pageable pageable);

    Page<TaxModel> findAllPaginated(String name, boolean active, Pageable pageable);

    // ========== Conteos ==========
    long count();

    long countByActive(boolean active);
}