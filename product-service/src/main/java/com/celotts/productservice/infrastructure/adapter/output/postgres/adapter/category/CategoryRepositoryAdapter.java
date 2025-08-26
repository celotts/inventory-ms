package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryJpaRepository;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component("categoryAdapter")
public class CategoryRepositoryAdapter implements CategoryRepositoryPort   {
    private final CategoryJpaRepository jpa;
    private final CategoryEntityMapper mapper;

    @Override
    public CategoryModel save(CategoryModel category) {
        CategoryEntity saved = jpa.save(mapper.toEntity(category));
        return mapper.toModel(saved);
    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<CategoryModel> findByName(String name) {
        return jpa.findByNameIgnoreCase(name).map(mapper::toModel);
    }

    @Override
    public List<CategoryModel> findAll() {
        return mapper.toModelList(jpa.findAll());
    }

    @Override
    public List<CategoryModel> findAllById(List<UUID> ids) {
        return mapper.toModelList(jpa.findAllById(ids));
    }

    @Override
    public List<CategoryModel> findByNameContaining(String name) {
        return mapper.toModelList(jpa.findByNameContainingIgnoreCase(name));
    }

    @Override
    public boolean existsByName(String name) {
        // usa el que realmente tengas en el repo
        return jpa.existsByName(name);               // o: jpa.existsByNameIgnoreCase(name)
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }

    @Override
    public Page<CategoryModel> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findByNameContaining(String name, Pageable pageable) {
        return jpa.findByNameContainingIgnoreCase(name, pageable)
                .map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findByActive(Boolean active, Pageable pageable) {
        return jpa.findByActive(active, pageable).map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        return jpa.findByNameContainingIgnoreCaseAndActive(name, active, pageable)
                .map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        final String q = normalize(name); // null → null, "  " → null, "TeXt  " → "TeXt"

        if (q != null && active != null) {
            return findByNameContainingAndActive(q, active, pageable);
        }
        if (q != null) {
            return findByNameContaining(q, pageable);
        }
        if (active != null) {
            return findByActive(active, pageable);
        }
        return findAll(pageable);
    }

    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    @Override
    public long count() {
        return jpa.count();
    }

    @Override
    public long countByActive(boolean active) {
        return jpa.countByActive(active);
    }

    @Override
    public List<CategoryModel> findByNameOrDescription(String term, int limit) {
        return mapper.toModelList(
                jpa.findByNameOrDescriptionContainingIgnoreCase(term.toLowerCase(), Pageable.ofSize(limit))
        );
    }
}