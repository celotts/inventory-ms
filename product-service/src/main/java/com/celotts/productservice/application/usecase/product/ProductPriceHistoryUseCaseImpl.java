package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.domain.port.input.product.ProductPriceHistoryUseCase;
import com.celotts.productservice.domain.port.output.product.ProductPriceHistoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductPriceHistoryUseCaseImpl implements ProductPriceHistoryUseCase {

    private final ProductPriceHistoryRepositoryPort repo;
    private final MessageSource messageSource; // ✅ inyección para i18n

    private String getLocalizedMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }


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
                .orElseThrow(() -> {
                    // Usamos tu método de utilidad
                    String msg = getLocalizedMessage("product.price.history.not-found", productId);
                    return new IllegalArgumentException(msg);
                });
    }
}