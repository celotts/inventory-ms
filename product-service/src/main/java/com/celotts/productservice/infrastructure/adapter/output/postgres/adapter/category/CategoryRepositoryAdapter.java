package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("categoryRepositoryAdapter")
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    // ========== MÉTODOS BÁSICOS CRUD ==========

    @Override
    public CategoryModel save(CategoryModel category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);
        return categoryEntityMapper.toDomain(categoryRepository.save(categoryEntity));
    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        return categoryRepository.findById(id).map(categoryEntityMapper::toDomain);
    }

    @Override
    public List<CategoryModel> findAll() {
        return categoryEntityMapper.toDomainList(categoryRepository.findAll());
    }

    @Override
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }

    // ========== MÉTODOS DE BÚSQUEDA ==========

    @Override
    public Optional<CategoryModel> findByName(String name) {
        return categoryRepository.findByName(name).map(categoryEntityMapper::toDomain);
    }

    @Override
    public List<CategoryModel> findByNameContaining(String name) {
        return categoryEntityMapper.toDomainList(categoryRepository.findByNameContaining(name));
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public List<CategoryModel> findByActive(Boolean active) {
        return categoryEntityMapper.toDomainList(categoryRepository.findByActive(active));
    }

    // ========== MÉTODOS CON PAGINACIÓN ==========

    @Override
    public Page<CategoryModel> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryEntityMapper::toDomain);
    }

    @Override
    public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
        return categoryRepository.findByActive(active, pageable).map(categoryEntityMapper::toDomain);
    }

    @Override
    public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
        return categoryRepository.findByNameContaining(name, pageable).map(categoryEntityMapper::toDomain);
    }

    @Override
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        return categoryRepository.findByNameContainingAndActive(name, active, pageable).map(categoryEntityMapper::toDomain);
    }

    // ========== MÉTODOS ADICIONALES ==========

    @Override
    public List<CategoryModel> findAllById(Iterable<UUID> ids) {
        return categoryEntityMapper.toDomainList(categoryRepository.findAllById(ids));
    }

    @Override
    public List<CategoryModel> findByNameOrDescription(String term, int limit) {
        return categoryEntityMapper.toDomainList(categoryRepository.findByNameOrDescription(term, limit));
    }

    @Override
    public long countByActive(Boolean active) {
        return categoryRepository.countByActive(active);
    }

    @Override
    public List<CategoryModel> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return categoryEntityMapper.toDomainList(categoryRepository.findByCreatedAtBetween(start, end));
    }

    @Override
    public boolean isCategoryInUse(UUID categoryId) {
        // Implementar según tu lógica de negocio
        // Por ejemplo, verificar si hay productos asociados
        return categoryRepository.existsById(categoryId);
    }
}