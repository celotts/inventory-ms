package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.port.product.ProductTypePort;

import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductTypeAdapter implements ProductTypePort {

    private final ProductTypeRepository productTypeRepository;

    @Override
    public boolean existsByCode(String code) {
        return productTypeRepository.existsByCode(code);
    }
    //TODO: cannot resolve method
    @Override
    public Optional<String> findNameByCode(String code) {
        return productTypeRepository.findNameByCode(code);
    }
    //TODO: cannot resolve method
    @Override
    public List<String> findAllCodes() {
        return productTypeRepository.findAll().stream()
                .map(productType -> productType.getCode() + " (" + productType.getName() + ")")
                .toList();
    }
}