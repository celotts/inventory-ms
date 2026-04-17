package com.celotts.salesservice.application.service;

import com.celotts.salesservice.domain.model.SaleModel;
import com.celotts.salesservice.domain.model.SaleStatus;
import com.celotts.salesservice.domain.port.input.SaleUseCase;
import com.celotts.salesservice.domain.port.output.ProductClientPort;
import com.celotts.salesservice.domain.port.output.SaleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService implements SaleUseCase {

    private final SaleRepositoryPort saleRepository;
    private final ProductClientPort productClient;

    @Override
    @Transactional
    public SaleModel createSale(SaleModel sale) {
        sale.setSaleDate(LocalDateTime.now());
        sale.setStatus(SaleStatus.PENDING);
        
        // Calcular total si no viene
        BigDecimal total = sale.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sale.setTotalAmount(total);

        SaleModel savedSale = saleRepository.save(sale);

        // Descontar inventario por cada item
        sale.getItems().forEach(item -> {
            productClient.discountStock(
                item.getProductId(), 
                new BigDecimal(item.getQuantity()), 
                "SALE-" + savedSale.getOrderNumber()
            );
        });

        return savedSale;
    }

    @Override
    public SaleModel findById(UUID id) {
        return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
    }

    @Override
    public List<SaleModel> findAll() {
        return saleRepository.findAll();
    }

    @Override
    @Transactional
    public void completeSale(UUID id) {
        SaleModel sale = findById(id);
        sale.setStatus(SaleStatus.COMPLETED);
        saleRepository.save(sale);
    }
}