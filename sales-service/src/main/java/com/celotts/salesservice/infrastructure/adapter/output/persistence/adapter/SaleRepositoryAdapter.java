package com.celotts.salesservice.infrastructure.adapter.output.persistence.adapter;

import com.celotts.salesservice.domain.model.SaleModel;
import com.celotts.salesservice.domain.port.output.SaleRepositoryPort;
import com.celotts.salesservice.infrastructure.adapter.input.rest.mapper.SaleMapper;
import com.celotts.salesservice.infrastructure.adapter.output.persistence.entity.SaleEntity;
import com.celotts.salesservice.infrastructure.adapter.output.persistence.repository.JpaSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SaleRepositoryAdapter implements SaleRepositoryPort {

    private final JpaSaleRepository jpaSaleRepository;
    private final SaleMapper saleMapper;

    @Override
    public SaleModel save(SaleModel sale) {
        if (sale.getId() == null) {
            sale.setId(UUID.randomUUID());
        }
        
        SaleEntity entity = saleMapper.toEntity(sale);
        
        // Asegurar la relación bidireccional para que JPA guarde los items correctamente
        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setSale(entity));
        }
        
        SaleEntity savedEntity = jpaSaleRepository.save(entity);
        return saleMapper.toModel(savedEntity);
    }

    @Override
    public Optional<SaleModel> findById(UUID id) {
        return jpaSaleRepository.findById(id).map(saleMapper::toModel);
    }

    @Override
    public List<SaleModel> findAll() {
        return saleMapper.toModelList(jpaSaleRepository.findAll());
    }
}