package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypeJpaRepository extends JpaRepository<ProductTypeEntity, String> {

    boolean existsByCode(String code);

    @Query("SELECT pt.name FROM ProductTypeEntity pt WHERE pt.code = :code")
    Optional<String> findNameByCode(@Param("code") String code);

    @Query("SELECT pt.code FROM ProductTypeEntity pt")
    List<String> findAllCodes();

    Optional<ProductTypeEntity> findByCode(String code);
}