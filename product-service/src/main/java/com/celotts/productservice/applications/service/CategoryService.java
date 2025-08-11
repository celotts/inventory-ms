package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.usecase.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatusDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@Slf4j
public class CategoryService {

    private final CategoryUseCase categoryUseCase;
    private final CategoryRequestMapper categoryRequestMapper;

    // Constructor explícito con @Qualifier
    public CategoryService(
            CategoryUseCase categoryUseCase,
            CategoryRequestMapper categoryRequestMapper) {
        this.categoryUseCase = categoryUseCase;
        this.categoryRequestMapper = categoryRequestMapper;
    }

    public CategoryModel create(CategoryModel dto) {
        if (categoryUseCase.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Category with name '" + dto.getName() + "' already exists");
        }

        CategoryModel category = categoryRequestMapper.toModel(dto);
        category.setCreatedAt(LocalDateTime.now());
        category.setCreatedBy(getCurrentUsername());
        return categoryUseCase.save(category);
    }

    public CategoryModel update(UUID id, CategoryModel dto) {
        CategoryModel existing = categoryUseCase.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));

        CategoryRequestMapper.updateModelFromDto(existing, dto);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(getCurrentUsername());

        return categoryUseCase.save(existing);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryModel> findById(UUID id) {
        return categoryUseCase.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryModel> findAll() {
        return categoryUseCase.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CategoryModel> findByName(String name) {
        return categoryUseCase.findByName(name);
    }

    public void deleteById(UUID id) {
        if (!categoryUseCase.existsById(id)) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }
        categoryUseCase.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return categoryUseCase.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryUseCase.existsByName(name);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }

    @Transactional(readOnly = true)
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        return categoryUseCase.findAllPaginated(name, active, pageable);
    }

    @Transactional(readOnly = true)
    public List<CategoryModel> searchByNameOrDescription(String query, int limit) {
        return categoryUseCase.searchByNameOrDescription(query, limit);
    }

    @Transactional
    public CategoryModel updateStatus(UUID id, Boolean active) {
        var category = categoryUseCase.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));
        category.setActive(active);
        category.setUpdatedAt(LocalDateTime.now());
        category.setUpdatedBy(getCurrentUsername());
        return categoryUseCase.save(category);
    }

    @Transactional
    public void permanentDelete(UUID id) {
        categoryUseCase.permanentDelete(id);
    }

    @Transactional
    public CategoryModel restore(UUID id) {
        return categoryUseCase.restore(id);
    }

    @Transactional(readOnly = true)
    public CategoryStatusDto getCategoryStatistics() {
        return categoryUseCase.getCategoryStatistics();
    }
}