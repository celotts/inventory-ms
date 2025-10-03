package com.celotts.productservice.domain.port.input.inventory;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.views.LotAvailableView;
import com.celotts.productservice.domain.model.views.LotExpiredView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface LotUseCase {
    LotModel create(LotModel model);
    Page<LotModel> listByProduct(UUID productId, Pageable pageable);
    Instant dispose(UUID lotId, String reference, String reason, String user); // ← cambia retorno
    Page<LotModel> markExpiredAndList(int horizonDays, Pageable pageable);

    Page<LotAvailableView> listLotsAvailable(UUID productId, Pageable pageable);
    Page<LotExpiredView> listLotsExpired(UUID productId, Pageable pageable);
}