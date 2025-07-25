package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.domain.port.product.port.usecase.ProductCategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product.ProductCategoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryService implements ProductCategoryUseCase {

    private final ProductCategoryAdapter adapter;

    @Override
    public ProductCategoryModel assignCategoryToProduct(ProductCategoryCreateDto dto) {
        ProductCategoryModel model = ProductCategoryModel.builder()
                .productId(dto.getProductId())
                .categoryId(dto.getCategoryId())
                .assignedAt(dto.getAssignedAt())
                .enabled(dto.getEnabled())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();

        return adapter.save(model);
    }

    @Override
    public ProductCategoryModel getById(UUID id) {
        return adapter.getById(id);
    }

    @Override
    public List<ProductCategoryModel> getAll() {
        return adapter.getAll();
    }

    @Override
    public void deleteById(UUID id) {
        adapter.deleteById(id);
    }

    @Override
    public void disableById(UUID id) {
        adapter.disableById(id);
    }
}