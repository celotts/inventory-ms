package com.celotts.productservice.application.usecase.stock;

import com.celotts.productservice.domain.exception.product.ProductNotFoundException;
import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.port.input.stock.ReceiveStockUseCase;
import com.celotts.productservice.domain.port.output.lot.LotRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.stock.StockReceptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiveStockUseCaseImpl implements ReceiveStockUseCase {

    private final ProductRepositoryPort productRepository;
    private final LotRepositoryPort lotRepository;

    @Override
    @Transactional
    public LotModel receiveStock(StockReceptionDto command) {
        log.info("Receiving stock for product: {}", command.getProductId());

        // 1. Validate Product
        ProductModel product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(command.getProductId()));

        // 2. Create Lot
        LotModel lot = LotModel.builder()
                .id(UUID.randomUUID())
                .productId(product.getId())
                .supplierId(command.getSupplierId())
                .lotCode(command.getLotCode() != null ? command.getLotCode() : generateLotCode(product))
                .quantity(command.getQuantity())
                .unitCost(command.getUnitCost())
                .expirationDate(command.getExpirationDate())
                .receivedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .enabled(true)
                // .stage(LotStage.AVAILABLE) // Assuming default or handled by mapper/DB
                .build();

        LotModel savedLot = lotRepository.save(lot);

        // 3. Update Product Stock (Simple increment for now)
        // In a more complex system, this would be an aggregation of lots, but for MVP:
        int newStock = (product.getCurrentStock() != null ? product.getCurrentStock() : 0) + command.getQuantity().intValue();
        productRepository.updateStock(product.getId(), newStock);

        log.info("Stock received. New stock for product {}: {}", product.getId(), newStock);

        return savedLot;
    }

    private String generateLotCode(ProductModel product) {
        return "LOT-" + product.getCode() + "-" + System.currentTimeMillis();
    }
}
