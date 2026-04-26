package com.celotts.salesservice.domain.port.output;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductClientPort {
    void discountStock(UUID productId, BigDecimal quantity, String reference);
    boolean checkStock(UUID productId, BigDecimal quantity);
}