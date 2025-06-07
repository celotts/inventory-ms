package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.port.product.ProductUnitPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductUnitRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductUnitAdapter implements ProductUnitPort {

    private final ProductUnitRepository productUnitRepository;

    @Override
    public boolean existsByCode(String code) {
        return productUnitRepository.existsByCode(code);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return productUnitRepository.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return productUnitRepository.findAll().stream()
                .map(unit -> unit.getCode() + " (" + unit.getName() + ")")
                .toList();
    }
}