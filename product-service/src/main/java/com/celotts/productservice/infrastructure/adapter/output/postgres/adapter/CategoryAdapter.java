package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.CategoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Category;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryPort {

    private final CategoryRepository repository;
    private final CategoryEntityMapper mapper;

    @Override
    public CategoryModel create(CategoryModel model) {
        Category entity = repository.save(mapper.toEntity(model));
        return mapper.toModel(entity);
    }

    @Override
    public List<CategoryModel> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public CategoryModel update(UUID id, CategoryModel model) {
        Category entity = mapper.toEntity(model);
        entity.setId(id);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}