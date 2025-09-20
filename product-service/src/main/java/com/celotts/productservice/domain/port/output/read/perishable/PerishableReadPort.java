package com.celotts.productservice.domain.port.output.read.perishable;

import com.celotts.productservice.domain.model.views.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PerishableReadPort {

    // Stock disponible (vista v_product_stock_available), filtro q opcional
    Page<ProductStockAvailableView> listProductStockAvailable(Pageable pageable, String q);

    // Próximos a vencer (usa la vista con horizonte fijo)
    Page<LotsExpiringSoonView> listLotsExpiringSoon(Pageable pageable);

    // Lotes disponibles por producto
    Page<LotAvailableView> listLotsAvailable(UUID productId, Pageable pageable);

    // Lotes vencidos por producto
    Page<LotExpiredView> listLotsExpired(UUID productId, Pageable pageable);
}