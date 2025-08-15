package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public Optional<CategoryModel> findByName(String name) {               // ← IMPLEMENTADO
        return repository.findByNameIgnoreCase(name)                       // o findByName(name)
                .map(mapper::toModel);
    }

    @Override
    public Page<CategoryModel> findAll(Pageable pageable) {                // ← IMPLEMENTADO
        return repository.findAll(pageable).map(mapper::toModel);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<CategoryModel> searchByNameOrDescription(String term,
                                                         Pageable pageable) { // ← si el puerto lo pide
        return repository.searchByNameOrDescription(term, pageable)
                .map(mapper::toModel);
    }
}