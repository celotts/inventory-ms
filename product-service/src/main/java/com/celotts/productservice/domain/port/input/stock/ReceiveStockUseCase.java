package com.celotts.productservice.domain.port.input.stock;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.stock.StockReceptionDto;

public interface ReceiveStockUseCase {
    /**
     * Processes the reception of stock, creating a lot and updating product inventory.
     * @param command The reception details.
     * @return The created LotModel.
     */
    LotModel receiveStock(StockReceptionDto command);
}
