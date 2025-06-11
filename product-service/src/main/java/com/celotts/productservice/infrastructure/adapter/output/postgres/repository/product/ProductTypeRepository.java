// Crear: src/main/java/com/celotts/productservice/repository/ProductTypeRepository.java

package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {

    boolean existsByCode(String code);


    @Query("SELECT pt.name FROM ProductType pt WHERE pt.code = :code")
    Optional<String> findNameByCode(@Param("code") String code);
}