package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.domain.port.input.product.ProductCategoryUseCase;
import com.celotts.productservice.domain.port.output.product.ProductCategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCategoryUseCaseImpl implements ProductCategoryUseCase {

    private final ProductCategoryRepositoryPort repo;

    @Override
    public ProductCategoryModel assignCategoryToProduct(ProductCategoryCreateDto dto) {
        // Duplication guard
        if (repo.existsByProductIdAndCategoryId(dto.getProductId(), dto.getCategoryId())) {
            throw new IllegalArgumentException("La asignación productId-categoryId ya existe");
        }
        // Si ya tienes un mapper de aplicación, úsalo aquí.
        var model = ProductCategoryModel.builder()
                .productId(dto.getProductId())
                .categoryId(dto.getCategoryId())
                .assignedAt(dto.getAssignedAt())
                .enabled(Boolean.TRUE.equals(dto.getEnabled()))
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
        return repo.save(model);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCategoryModel getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductCategory not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryModel> getAll() {
        // TODO: replace with repo.findAll() once the port & adapter expose it
        return java.util.List.of();
    }

    @Override
    public void disableById(UUID id) {
        var current = getById(id);
        var disabled = current.toBuilder().enabled(false).build();
        repo.save(disabled);
    }

    @Override
    public void deleteById(UUID id) {
        repo.deleteById(id);
    }
}