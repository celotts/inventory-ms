package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPriceHistoryRepositoryPort {
    ProductPriceHistoryModel save(ProductPriceHistoryModel model);
    List<ProductPriceHistoryModel> findAllByProductIdOrderByChangedAtDesc(UUID productId);
    Optional<ProductPriceHistoryModel> findTopByProductIdOrderByChangedAtDesc(UUID productId);
}