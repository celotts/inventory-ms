package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.views.LotsExpiringSoonView;
import com.celotts.productservice.domain.model.views.ProductStockAvailableView;
import com.celotts.productservice.domain.port.output.read.perishable.PerishableReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final PerishableReadPort readPort;

    @GetMapping("/stock")
    public Page<ProductStockAvailableView> stock(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q
    ) {
        return readPort.listProductStockAvailable(PageRequest.of(page, size), q);
    }

    @GetMapping("/lots/expiring-soon")
    public Page<LotsExpiringSoonView> expiringSoon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return readPort.listLotsExpiringSoon(PageRequest.of(page, size));
    }
}