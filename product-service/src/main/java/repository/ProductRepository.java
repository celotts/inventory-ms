// ProductRepository - Repositorio JPA extendido
package repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Búsqueda por código
    Optional<Product> findByCode(String code);

    // Verificar existencia por código
    boolean existsByCode(String code);

    // Búsqueda por tipo de producto
    @Query("SELECT p FROM Product p WHERE p.productType.code = :productTypeCode")
    List<Product> findByProductTypeCode(@Param("productTypeCode") String productTypeCode);

    // Búsqueda por marca
    List<Product> findByProductBrandId(UUID brandId);

    // Búsqueda por estado (habilitado/deshabilitado)
    List<Product> findByEnabled(Boolean enabled);
    Page<Product> findByEnabled(Boolean enabled, Pageable pageable);

    // Conteo por estado
    long countByEnabled(Boolean enabled);

    // Productos con stock bajo el mínimo
    @Query("SELECT p FROM Product p WHERE p.currentStock < p.minimumStock")
    List<Product> findByCurrentStockLessThanMinimumStock();

    // Productos con stock menor a un valor específico
    List<Product> findByCurrentStockLessThan(Integer stock);

    // Productos con stock mayor a un valor específico
    List<Product> findByCurrentStockGreaterThan(Integer stock);

    // Búsqueda por descripción (contiene texto)
    @Query("SELECT p FROM Product p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Product> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    // Búsqueda por código o descripción
    @Query("SELECT p FROM Product p WHERE LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> findByCodeOrDescriptionContainingIgnoreCase(@Param("search") String search);

    // Productos por rango de precio
    @Query("SELECT p FROM Product p WHERE p.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByUnitPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Productos activos ordenados por fecha de creación
    @Query("SELECT p FROM Product p WHERE p.enabled = true ORDER BY p.createdAt DESC")
    List<Product> findActiveProductsOrderByCreatedAtDesc();

    // Productos con paginación y filtros complejos
    @Query("SELECT p FROM Product p WHERE " +
            "(:enabled IS NULL OR p.enabled = :enabled) AND " +
            "(:productTypeCode IS NULL OR p.productType.code = :productTypeCode) AND " +
            "(:brandId IS NULL OR p.productBrand.id = :brandId)")
    Page<Product> findWithFilters(
            @Param("enabled") Boolean enabled,
            @Param("productTypeCode") String productTypeCode,
            @Param("brandId") UUID brandId,
            Pageable pageable
    );
}