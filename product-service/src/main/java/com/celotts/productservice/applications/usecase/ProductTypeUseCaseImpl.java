package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.port.product.type.output.ProductTypeRepositoryPort;
import com.celotts.productservice.domain.port.product.type.usecase.ProductTypeUseCase;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class ProductTypeUseCaseImpl implements ProductTypeUseCase {

    private final ProductTypeRepositoryPort repo;

    @Override public boolean existsByCode(String code) { return repo.existsByCode(code); }
    @Override public Optional<String> findNameByCode(String code) { return repo.findNameByCode(code); }
    @Override public List<String> findAllCodes() { return repo.findAllCodes(); }
    @Override public Optional<ProductTypeEntity> findByCode(String code) { return repo.findByCode(code); }
}