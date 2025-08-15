package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("categoryAdapter")
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository repository;
    private final CategoryEntityMapper mapper;

    @Override
    public CategoryModel save(CategoryModel model) {
        CategoryEntity saved = repository.save(mapper.toEntity(model));
        return mapper.toModel(saved);
    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<CategoryModel> findByName(String name) {
        return repository.findByNameIgnoreCase(name).map(mapper::toModel);
    }

    // ======== LIST (no paginado) ========

    @Override
    public List<CategoryModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<CategoryModel> findAllById(List<UUID> ids) {
        return repository.findAllById(ids).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<CategoryModel> findByNameContaining(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(mapper::toModel).toList();
    }

    @Override
    public List<CategoryModel> findByNameOrDescription(String query, int limit) {
        return repository.findByNameOrDescriptionContainingIgnoreCase(query, Pageable.ofSize(limit)).stream()
                .map(mapper::toModel).toList();
    }

    // ======== DELETE / EXISTS ========

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    // ======== PAGINADO ========

    @Override
    public Page<CategoryModel> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable)
                .map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
        return repository.findByActive(active, pageable).map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        return repository.findByNameContainingIgnoreCaseAndActive(name, active, pageable)
                .map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        // Estrategia simple: enrutar según filtros presentes
        if (name != null && !name.isBlank() && active != null) {
            return findByNameContainingAndActive(name, active, pageable);
        } else if (name != null && !name.isBlank()) {
            return findByNameContaining(name, pageable);
        } else if (active != null) {
            return findByActive(active, pageable);
        }
        return findAll(pageable);
    }

    // ======== STATS ========

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countByActive(boolean active) {
        return repository.countByActive(active);
    }
}