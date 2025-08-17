package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.input.product.ProductUnitUseCase;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service                        // 👉 ahora Spring lo descubre como bean
@Transactional
@RequiredArgsConstructor
public class ProductUnitUseCaseImpl implements ProductUnitUseCase {

    private final ProductUnitRepositoryPort repo;   // puerto de SALIDA

    @Override
    public ProductUnitModel save(ProductUnitModel model) { return repo.save(model); }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductUnitModel> findById(UUID id) { return repo.findById(id); }


    @Override
    public ProductUnitModel update(UUID id, ProductUnitModel model) {
        // Asigna el ID recibido al modelo y persiste
        model.setId(id);
        return repo.save(model);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findDescriptionByCode(String code) {
        return repo.findDescriptionByCode(code);
    }

    public boolean existsById(UUID id){ return repo.findById(id).isPresent(); }

    @Override
    @Transactional(readOnly = true)
    public List<ProductUnitModel> findAll() {     /* tu lógica */
        // p.e. mapear repo.findAllEntities() a Model
        return repo.findAll();
    }

    @Override public void deleteById(UUID id){ repo.deleteById(id); }
   // @Override public boolean existsById(UUID id){ return repo.findById(id).isPresent(); }
    @Override public boolean existsByCode(String c){ return repo.existsByCode(c); }
    @Override public Optional<String> findNameByCode(String c){ return repo.findNameByCode(c);}
    @Override public List<String> findAllCodes(){ return repo.findAllCodes(); }
}