package com.celotts.productservice.domain.port.output.perishable;

import com.celotts.productservice.domain.model.views.*;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerishableReadPort {
    Page<ProductStockAvailableView> findProductStockAvailable(Pageable pageable, String q);
    Page<LotsExpiringSoonView> findLotsExpiringSoon(int days, Pageable pageable);
    Page<LotAvailableView> findLotsAvailable(UUID productId, Pageable pageable);
    Page<LotExpiredView> findLotsExpired(UUID productId, Pageable pageable);
}

