package com.celotts.productservice.domain.port.output.ai;

import java.util.List;

public interface GenerativeAiPort {
    String generateProductDescription(String productName, List<String> tags);
}