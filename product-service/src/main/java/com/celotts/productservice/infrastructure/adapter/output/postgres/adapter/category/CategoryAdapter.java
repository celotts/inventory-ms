package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryRepositoryPort {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;
    private final EntityManager entityManager;

    @Override
    public CategoryModel save(CategoryModel category) {
        var categoryEntity = categoryEntityMapper.toEntity(category);
        var savedEntity = categoryRepository.save(categoryEntity);
        return categoryEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        return categoryRepository.findById(id)
                .map(categoryEntityMapper::toDomain);
    }

    @Override
    public Optional<CategoryModel> findByName(String name) {
        return categoryRepository.findByName(name)
                .map(categoryEntityMapper::toDomain);
    }

    @Override
    public List<CategoryModel> findAll() {
        // CAMBIO 1: Usar Sort en lugar de método personalizado
        var entities = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return categoryEntityMapper.toDomainList(entities);
    }

    @Override
    public List<CategoryModel> findByNameContaining(String name) {
        var entities = categoryRepository.findByNameContaining(name);
        return categoryEntityMapper.toDomainList(entities);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }

    // ========== MÉTODOS NUEVOS AGREGADOS ==========

    @Override
    public List<CategoryModel> findByActive(Boolean active) {
        var entities = categoryRepository.findByActive(active);
        return categoryEntityMapper.toDomainList(entities);
    }

    @Override
    public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
        var entitiesPage = categoryRepository.findByActive(active, pageable);
        return entitiesPage.map(categoryEntityMapper::toDomain);
    }

    @Override
    public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
        // CAMBIO 2: Usar método sin IgnoreCase
        var entitiesPage = categoryRepository.findByNameContaining(name, pageable);
        return entitiesPage.map(categoryEntityMapper::toDomain);
    }

    @Override
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        // CAMBIO 3: Usar método sin IgnoreCase
        var entitiesPage = categoryRepository.findByNameContainingAndActive(name, active, pageable);
        return entitiesPage.map(categoryEntityMapper::toDomain);
    }

    @Override
    public Page<CategoryModel> findAll(Pageable pageable) {
        var entitiesPage = categoryRepository.findAll(pageable);
        return entitiesPage.map(categoryEntityMapper::toDomain);
    }

    @Override
    public List<CategoryModel> findAllById(Iterable<UUID> ids) {
        var entities = categoryRepository.findAllById(ids);
        return categoryEntityMapper.toDomainList(entities);
    }

    @Override
    public long countByActive(Boolean active) {
        return categoryRepository.countByActive(active);
    }

    @Override
    public List<CategoryModel> findByNameOrDescription(String term, int limit) {
        var results = entityManager.createQuery(
                        "FROM CategoryEntity c WHERE LOWER(c.name) LIKE :term OR LOWER(c.description) LIKE :term",
                        CategoryEntity.class)
                .setParameter("term", "%" + term.toLowerCase() + "%")
                .setMaxResults(limit)
                .getResultList();

        return categoryEntityMapper.toDomainList(results);
    }


    @Override
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        if (name != null && !name.isBlank() && active != null) {
            return categoryRepository.findByNameContainingAndActive(name, active, pageable)
                    .map(categoryEntityMapper::toDomain);
        } else if (name != null && !name.isBlank()) {
            return categoryRepository.findByNameContaining(name, pageable)
                    .map(categoryEntityMapper::toDomain);
        } else if (active != null) {
            return categoryRepository.findByActive(active, pageable)
                    .map(categoryEntityMapper::toDomain);
        } else {
            return categoryRepository.findAll(pageable).map(categoryEntityMapper::toDomain);
        }
    }
}