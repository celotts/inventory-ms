package com.celotts.productservice.domain.port.input.inventory;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.views.*;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LotUseCase {
    LotModel create(LotModel model);
    Page<LotModel> listByProduct(UUID productId, Pageable pageable);
    void dispose(UUID lotId, String reference, String reason, String user);
    Stream<LotsExpiringSoonView> markExpiredAndList(int horizonDays);

    Page<LotAvailableView> listLotsAvailable(UUID productId, Pageable pageable);
    Page<LotExpiredView> listLotsExpired(UUID productId, Pageable pageable);
}
