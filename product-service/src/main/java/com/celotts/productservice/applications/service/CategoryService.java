package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.category.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatsDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryRequestMapper;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Transactional(readOnly = true)
    public List<CategoryModel> findByNameContaining(String name) {
        return categoryUseCase.findByNameContaining(name);
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

    @Transactional(readOnly = true)
    public List<CategoryModel> findByActive(Boolean active) {
        return categoryUseCase.findByActive(active);
    }

    @Transactional(readOnly = true)
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        if (name != null && !name.trim().isEmpty() && active != null) {
            return categoryUseCase.findByNameContainingAndActive(name.trim(), active, pageable);
        } else if (name != null && !name.trim().isEmpty()) {
            return categoryUseCase.findByNameContaining(name.trim(), pageable);
        } else if (active != null) {
            return categoryUseCase.findByActive(active, pageable);
        } else {
            return categoryUseCase.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryModel> searchByNameOrDescription(String query, int limit) {
        if (query == null || query.trim().isEmpty()) {
            return categoryUseCase.findAll(Pageable.ofSize(limit)).getContent();
        }
        return categoryUseCase.findByNameOrDescription(query.trim().toLowerCase(), limit);
    }

    public CategoryModel updateStatus(UUID id, Boolean active) {
        CategoryModel existing = categoryUseCase.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));

        existing.setActive(active);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(getCurrentUsername());

        return categoryUseCase.save(existing);
    }

    public void permanentDelete(UUID id) {
        if (!categoryUseCase.existsById(id)) {
            throw new IllegalArgumentException("Category with ID " + id + " not found");
        }
        categoryUseCase.deleteById(id);
    }

    public CategoryModel restore(UUID id) {
        CategoryModel category = categoryUseCase.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found"));

        category.setActive(true);
        category.setDeleted(false);
        category.setUpdatedAt(LocalDateTime.now());
        category.setUpdatedBy(getCurrentUsername());

        return categoryUseCase.save(category);
    }

    @Transactional(readOnly = true)
    public CategoryStatsDto getCategoryStatistics() {
        List<CategoryModel> allCategories = categoryUseCase.findAll();

        long total = allCategories.size();
        long active = allCategories.stream().filter(c -> Boolean.TRUE.equals(c.getActive())).count();
        long inactive = allCategories.stream().filter(c -> Boolean.FALSE.equals(c.getActive())).count();
        long deleted = allCategories.stream().filter(CategoryModel::isDeleted).count();

        return CategoryStatsDto.builder()
                .totalCategories(total)
                .activeCategories(active)
                .inactiveCategories(inactive)
                .deletedCategories(deleted)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CategoryModel> findByIds(List<UUID> ids) {
        return categoryUseCase.findAllById(ids);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public List<CategoryModel> sortCategories(List<CategoryModel> categories, String sortBy, String sortDirection) {
        if (categories == null || categories.isEmpty()) {
            return categories;
        }

        Comparator<CategoryModel> comparator = switch (sortBy != null ? sortBy.toLowerCase() : "") {
            case "name" -> Comparator.comparing(CategoryModel::getName, String.CASE_INSENSITIVE_ORDER);
            case "create" -> Comparator.comparing(CategoryModel::getCreatedAt);
            case "update" ->
                    Comparator.comparing(CategoryModel::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            case "active" ->
                    Comparator.comparing(CategoryModel::getActive, Comparator.nullsLast(Comparator.naturalOrder()));
            default ->
                    Comparator.comparing(CategoryModel::getName, String.CASE_INSENSITIVE_ORDER);
        };

        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        return categories.stream()
                .sorted(comparator)
                .toList();
    }
}