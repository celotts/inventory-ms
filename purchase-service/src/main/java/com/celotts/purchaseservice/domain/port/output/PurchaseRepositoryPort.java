package com.celotts.purchaseservice.domain.port.output;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

public interface PurchaseRepositoryPort {
    PurchaseModel save(PurchaseModel purchase);
    Optional<PurchaseModel> findById(UUID id);
    Optional<PurchaseModel> findByCode(String code);
    boolean existsById(UUID id);
    boolean existsByName(String name);
    boolean existsByCode(String code);
    Page<PurchaseModel> findAll(Pageable pageable);
    Page<PurchaseModel> findByNameContaining(String name, Pageable pageable);
    Page<PurchaseModel> findByActive(Boolean active);
    List<PurchaseModel> findByNameDescription(String query, int limit);
    void deleteById(UUID id);
}