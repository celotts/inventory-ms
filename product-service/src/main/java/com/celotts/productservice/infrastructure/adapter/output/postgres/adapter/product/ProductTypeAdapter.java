package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductTypeModel;
import com.celotts.productservice.domain.port.output.product.ProductTypeRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductTypeEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("productTypeAdapter") // <- Nombre opcional para usar con @Qualifier
@RequiredArgsConstructor
public class ProductTypeAdapter implements ProductTypeRepositoryPort {

    private final ProductTypeJpaRepository repository;
    private final ProductTypeEntityMapper mapper;

    @Override
    public ProductTypeModel save(ProductTypeModel model) {
        ProductTypeEntity entity = mapper.toEntity(model);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    public Optional<ProductTypeModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<ProductTypeModel> findByCode(String code) {
        return repository.findByCode(code).map(mapper::toModel);
    }

    @Override
    public List<ProductTypeModel> findAll() {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return repository.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return repository.findAllCodes();
    }
}