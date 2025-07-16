package com.celotts.productservice.domain.port.category.usecase;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatsDto;
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
    Page<CategoryModel> findAll(Pageable pageable);
    List<CategoryModel> findByNameContaining(String name); // Si lo usas, si no, elimínalo
    boolean existsByName(String name);
    boolean existsById(UUID id);
    void deleteById(UUID id);

    List<CategoryModel> findAllById(List<UUID> ids);

    Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable);
    CategoryModel updateStatus(UUID id, Boolean active);
    CategoryModel restore(UUID id);
    void permanentDelete(UUID id);
    CategoryStatsDto getCategoryStatistics();

    List<CategoryModel> findByNameOrDescription(String query, int limit);

    List<CategoryModel> searchByNameOrDescription(String query, int limit);
    List<CategoryModel> findByActive(Boolean active); // Si lo usas, si no, elimínalo
    /**
     * Devuelve categorías paginadas filtradas por nombre y/o estado activo.
     * Este método depende internamente de los métodos:
     * - findByNameContaining
     * - findByActive
     * - findByNameContainingAndActive
     *
     * ⚠️ No eliminar esos métodos del puerto o repositorio aunque el IDE los marque sin usos directos.
     */
    Page<CategoryModel> findByNameContaining(String name, Pageable pageable); // Mantén si se usa
    Page<CategoryModel> findByActive(Boolean active, Pageable pageable); // Mantén si se usa
    Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable); // Mantén si se usa

    long countByActive(boolean active);

}