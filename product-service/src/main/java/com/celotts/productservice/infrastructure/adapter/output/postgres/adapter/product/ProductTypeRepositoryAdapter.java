package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.domain.port.output.product.ProductTypeRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductTypeEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductTypeRepositoryAdapter implements ProductTypeRepositoryPort {

    private final ProductTypeJpaRepository jpaRepo;
    private final ProductTypeEntityMapper mapper;

    @Override
    @Transactional
    public ProductTypeModel save(ProductTypeModel model) {
        ProductTypeEntity entity = mapper.toEntity(model);
        return mapper.toModel(jpaRepo.save(entity));
    }

    @Override
    public Optional<ProductTypeModel> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel); // ← sin toString()
    }

    @Override
    public Optional<ProductTypeModel> findByCode(String code) {
        return jpaRepo.findByCode(code).map(mapper::toModel);
    }

    @Override
    public Page<ProductTypeModel> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable).map(mapper::toModel); // ← IMPLEMENTADO
    }

    @Override
    public List<ProductTypeModel> findAll() {
        return jpaRepo.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id); // ← sin toString()
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepo.existsByCode(code);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return jpaRepo.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return jpaRepo.findAllCodes();
    }
}