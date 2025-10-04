package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.views.LotsExpiringSoonView;
import com.celotts.productservice.domain.model.views.ProductStockAvailableView;
import com.celotts.productservice.domain.port.output.read.perishable.PerishableReadPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final PerishableReadPort readPort;

    @GetMapping("/stock")
    public ResponseEntity<PageResponse<ProductStockAvailableView>> stock(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q
    ) {
        var pg = readPort.listProductStockAvailable(PageRequest.of(page, size), q);
        return ResponseEntity.ok(PageResponse.from(pg));
    }

    // expiringSoon()
    @GetMapping("/lots/expiring-soon")
    public ResponseEntity<PageResponse<LotsExpiringSoonView>> expiringSoon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var pg = readPort.listLotsExpiringSoon(PageRequest.of(page, size));
        return ResponseEntity.ok(PageResponse.from(pg));
    }
}