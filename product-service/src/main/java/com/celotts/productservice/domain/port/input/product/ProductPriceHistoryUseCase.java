package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;

import java.util.List;
import java.util.UUID;

public interface ProductPriceHistoryUseCase {
    ProductPriceHistoryModel create(ProductPriceHistoryModel model);
    List<ProductPriceHistoryModel> getByProduct(UUID productId);
    ProductPriceHistoryModel getLatestPrice(UUID productId);
}