package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.domain.port.input.product.ProductTypeUseCase;
import com.celotts.productservice.domain.port.output.product.ProductTypeRepositoryPort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;   // <-- import necesario
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeUseCaseImpl implements ProductTypeUseCase {

    private final ProductTypeRepositoryPort productTypeRepository;

    public ProductTypeUseCaseImpl(
            @Qualifier("productTypeRepositoryAdapter")  // <-- el bean que quieres
            ProductTypeRepositoryPort productTypeRepository
    ) {
        this.productTypeRepository = productTypeRepository;
    }

    @PostConstruct
    public void log() { System.out.println("⚠️ ProductTypeUseCaseImpl fue cargado."); }

    @Override public boolean existsByCode(String code) { return productTypeRepository.existsByCode(code); }
    @Override public Optional<String> findNameByCode(String code) { return productTypeRepository.findNameByCode(code); }
    @Override public List<String> findAllCodes() { return productTypeRepository.findAllCodes(); }
    @Override public Optional<ProductTypeModel> findByCode(String code) { return productTypeRepository.findByCode(code); }
}