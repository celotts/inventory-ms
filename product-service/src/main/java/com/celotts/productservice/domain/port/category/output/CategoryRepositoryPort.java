package com.celotts.productservice.domain.port.category.output;

import com.celotts.productservice.domain.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryPort {

    // ========== MÉTODOS BÁSICOS CRUD ==========
    CategoryModel save(CategoryModel category);
    Optional<CategoryModel> findById(UUID id);
    List<CategoryModel> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);

    // ========== MÉTODOS DE BÚSQUEDA ==========
    Optional<CategoryModel> findByName(String name);
    List<CategoryModel> findByNameContaining(String name);
    boolean existsByName(String name);
    List<CategoryModel> findByActive(Boolean active);

    // ========== MÉTODOS CON PAGINACIÓN ==========
    Page<CategoryModel> findAll(Pageable pageable);
    Page<CategoryModel> findByActive(Boolean active, Pageable pageable);
    Page<CategoryModel> findByNameContaining(String name, Pageable pageable);
    Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);
    Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable);
    // ========== MÉTODOS ADICIONALES ==========
    List<CategoryModel> findAllById(Iterable<UUID> ids);
    List<CategoryModel> findByNameOrDescription(String term, int limit);
    long countByActive(Boolean active);




}