package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;



import com.celotts.productservice.domain.port.product.type.output.ProductTypeRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component("productTypeRepositoryAdapter")
@RequiredArgsConstructor
public class ProductTypeRepositoryAdapter implements ProductTypeRepositoryPort {

    private final ProductTypeRepository repo;

    @Override public boolean existsByCode(String code){ return repo.existsByCode(code); }
    @Override public Optional<String> findNameByCode(String code){ return repo.findNameByCode(code); }
    @Override public List<String> findAllCodes(){ return repo.findAllCodes(); }
    @Override public Optional<ProductTypeEntity> findByCode(String code){ return repo.findByCode(code); }
}