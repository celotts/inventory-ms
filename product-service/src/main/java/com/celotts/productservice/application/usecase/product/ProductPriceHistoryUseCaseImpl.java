package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.domain.port.input.product.ProductPriceHistoryUseCase;
import com.celotts.productservice.domain.port.output.product.ProductPriceHistoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductPriceHistoryUseCaseImpl implements ProductPriceHistoryUseCase {

    private final ProductPriceHistoryRepositoryPort repo;

    @Override
    public ProductPriceHistoryModel create(ProductPriceHistoryModel model) {
        var toSave = model.toBuilder()
                .enabled(model.getEnabled() == null ? Boolean.TRUE : model.getEnabled())
                .changedAt(model.getChangedAt() == null ? LocalDateTime.now() : model.getChangedAt())
                .build();
        return repo.save(toSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceHistoryModel> getByProduct(UUID productId) {
        return repo.findAllByProductIdOrderByChangedAtDesc(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPriceHistoryModel getLatestPrice(UUID productId) {
        return repo.findTopByProductIdOrderByChangedAtDesc(productId)
                .orElseThrow(() -> new IllegalArgumentException("No price history for product " + productId));
    }
}