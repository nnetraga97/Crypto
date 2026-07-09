package com.nnetraga.crypto.settlement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nnetraga.crypto.settlement.SettlementIntent;
import com.nnetraga.crypto.settlement.SettlementService;
import com.nnetraga.crypto.settlement.dto.CreateSettlementIntentRequest;

@RestController
public class SettlementController {
    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @PostMapping("/settlement-intents")
    @ResponseStatus(HttpStatus.CREATED)
    public SettlementIntent create(@RequestBody CreateSettlementIntentRequest request) {
        SettlementIntent intent = SettlementIntent.create(
                request.settlementId(),
                request.idempotencyKey(),
                request.amount(),
                request.asset(),
                request.destinationAddress());
        return settlementService.submit(intent);
    }
}
