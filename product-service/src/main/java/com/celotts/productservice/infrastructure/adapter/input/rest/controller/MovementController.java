package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.port.input.inventory.MovementUseCase;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementUseCase movementUseCase;

    @PostMapping("/in")
    public Object registerIn(@RequestParam UUID productId,
                             @RequestParam(required = false) UUID lotId,
                             @RequestParam @Positive BigDecimal quantity,
                             @RequestParam(required = false) String reference,
                             @RequestParam(defaultValue = "api") String user) {
        return movementUseCase.registerIn(productId, lotId, quantity, reference, user);
    }

    @PostMapping("/adjust")
    public Object adjust(@RequestParam UUID productId,
                         @RequestParam(required = false) UUID lotId,
                         @RequestParam @Positive BigDecimal quantity,
                         @RequestParam String reason,
                         @RequestParam(defaultValue = "api") String user) {
        return movementUseCase.registerAdjust(productId, lotId, quantity, reason, user);
    }
}
