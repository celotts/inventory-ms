package com.celotts.taxservice.domain.port.output;

import com.celotts.taxservice.domain.model.TaxModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaxRepositoryPort {

    TaxModel save(TaxModel category);

    Optional<TaxModel> findById(UUID id);
    Optional<TaxModel> findByName(String name);
    List<TaxModel> findAll();
    List<TaxModel> findById(List<UUID> ids);

    List<TaxModel> findByNameContaining(String name);
    List<TaxModel> findByNameOrDescription(String query, int limit);

    void deleteById(UUID id);

    boolean existsById(UUID id);
    boolean existsByName(String name);

    Page<TaxModel> findAll(Pageable pageable);
    Page<TaxModel> findByNameContaining(String name, Pageable pageable);
    Page<TaxModel> findByActive(Boolean active, Pageable pageable);
    Page<TaxModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);
    Page<TaxModel> findAllPaginated(String name, boolean active, Pageable pageable);

    long count();
    long countByActive(boolean active);
}
