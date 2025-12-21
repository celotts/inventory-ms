package com.celotts.taxservice.domain.contract.tax;

import java.math.BigDecimal;
import java.util.UUID;

public class TaxReference {
    UUID id;
    String code;
    String name;
    BigDecimal rate;
}
