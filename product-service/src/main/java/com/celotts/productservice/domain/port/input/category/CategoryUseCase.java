package com.celotts.productservice.domain.port.input.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.model.CategoryStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryUseCase {

    CategoryModel save(CategoryModel category);

    Optional<CategoryModel> findById(UUID id);
    Optional<CategoryModel> findByName(String name);
    List<CategoryModel> findAll();
    List<CategoryModel> findAllById(List<UUID> ids);

    List<CategoryModel> findByNameContaining(String name);
    List<CategoryModel> searchByNameOrDescription(String query, int limit);

    void deleteById(UUID id);

    boolean existsById(UUID id);
    boolean existsByName(String name);

    Page<CategoryModel> findAll(Pageable pageable);
    Page<CategoryModel> findByNameContaining(String name, Pageable pageable);
    Page<CategoryModel> findByActive(Boolean active, Pageable pageable);
    Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);
    Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable);

    CategoryModel updateStatus(UUID id, Boolean active);
    CategoryModel restore(UUID id);
    void permanentDelete(UUID id);

    CategoryStats getCategoryStatistics();
    long countByActive(boolean active);
}