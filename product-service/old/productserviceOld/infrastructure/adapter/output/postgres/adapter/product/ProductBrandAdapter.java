package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productserviceOld.domain.model.ProductBrandModel;
import com.celotts.productserviceOld.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productserviceOld.domain.port.product.brand.output.ProductBrandRepositoryPort;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product.ProductBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("productBrandAdapter")
@RequiredArgsConstructor
public class ProductBrandAdapter implements ProductBrandRepositoryPort, ProductBrandPort {

    private final ProductBrandRepository productBrandRepository;
    private final ProductBrandEntityMapper mapper;

    @Override
    public ProductBrandModel save(ProductBrandModel productBrand) {
        ProductBrandEntity entity = mapper.toEntity(productBrand);
        ProductBrandEntity saved = productBrandRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<ProductBrandModel> findById(UUID id) {
        return productBrandRepository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public Optional<ProductBrandModel> findByName(String name) {
        return productBrandRepository.findByName(name)
                .map(mapper::toModel);
    }

    @Override
    public Optional<String> findNameById(UUID id) {
        return productBrandRepository.findNameById(id);
    }

    @Override
    public List<ProductBrandModel> findAll() {
        return productBrandRepository.findAll().stream()
                .map(mapper::toModel)
                .toList();
    }
    
    @Override
    public List<UUID> findAllIds() {
        return productBrandRepository.findAll().stream()
                .map(ProductBrandEntity::getId)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return productBrandRepository.existsByName(name);
    }

    @Override
    public boolean existsById(UUID id) {
        return productBrandRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        productBrandRepository.deleteById(id);
    }

    @Override
    public ProductBrandResponseDto enableBrand(UUID id) {
        productBrandRepository.enableBrandById(id);
        ProductBrandEntity entity = productBrandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product brand not found: " + id));
        return mapper.toResponseDto(entity);
    }

    @Override
    public ProductBrandResponseDto disableBrand(UUID id) {
        productBrandRepository.disableBrandById(id);
        ProductBrandEntity entity = productBrandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product brand not found: " + id));
        return mapper.toResponseDto(entity);
    }
}