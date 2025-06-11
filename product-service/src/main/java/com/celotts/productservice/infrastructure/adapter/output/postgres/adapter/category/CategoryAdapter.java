package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryRepositoryPort {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    // ========== MÉTODOS EXISTENTES (mantenidos) ==========

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
        var entities = categoryRepository.findAllOrderByCreatedAtDesc();
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
        var entitiesPage = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
        return entitiesPage.map(categoryEntityMapper::toDomain);
    }

    @Override
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        var entitiesPage = categoryRepository.findByNameContainingIgnoreCaseAndActive(name, active, pageable);
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
}