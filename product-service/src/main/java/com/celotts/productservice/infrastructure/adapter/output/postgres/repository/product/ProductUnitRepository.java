package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnitEntity, String> {
    
    boolean existsByCode(String code);
    
    Optional<ProductUnitEntity> findByCode(String code);
    
    @Query("SELECT p.name FROM ProductUnitEntity p WHERE p.code = :code")
    Optional<String> findNameByCode(@Param("code") String code);
}
