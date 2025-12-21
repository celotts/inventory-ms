package com.celotts.taxservice.domain.port.input.tax;

import com.celotts.taxservice.domain.model.tax.TaxModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaxUseCase {

    TaxModel save(TaxModel tax);

    Optional<TaxModel> findById(UUID id);
    Optional<TaxModel> findByName(String name);
    List<TaxModel> findAll();
    List<TaxModel> findAllById(List<UUID> ids);

    List<TaxModel> findByNameContaining(String name);
    List<TaxModel> findByDescriptionContaining(String name);

    List<TaxModel> finjdByNameContaining(String name);

    List<TaxModel> searchByNameOrDescriptionm(String query, int limit);

    void deleteById(UUID id);

    boolean existById(UUID id);
    boolean existByName(String name);

    Page<TaxModel> findAll(Pageable pageable);
    Page<TaxModel> findByNameContaining(String name, Pageable pageable);
    Page<TaxModel> findByActive(Boolean active, Pageable pageable);
    Page<TaxModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);
    Page<TaxModel> findAllPaginated(String name, Boolean active,  Pageable pageable);

    TaxModel create(TaxModel model);
}
