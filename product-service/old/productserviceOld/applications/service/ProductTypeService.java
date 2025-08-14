package com.celotts.productserviceOld.applications.service;

import com.celotts.productserviceOld.domain.port.product.type.input.ProductTypePort;
import com.celotts.productserviceOld.domain.port.product.type.usecase.ProductTypeUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ProductTypeService implements ProductTypePort {

    private final ProductTypeUseCase productTypeUseCase;

    @Override
    public boolean existsByCode(String code) {
        return productTypeUseCase.existsByCode(code);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return productTypeUseCase.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return productTypeUseCase.findAllCodes();
    }

    @Override
    public Optional<ProductTypeEntity> findByCode(String code) {
        return productTypeUseCase.findByCode(code);
    }



}
