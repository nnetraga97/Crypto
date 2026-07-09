package com.nnetraga.crypto.settlement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nnetraga.crypto.settlement.SettlementIntent;
import com.nnetraga.crypto.settlement.SettlementService;
import com.nnetraga.crypto.settlement.dto.ApiError;
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

    @GetMapping("/settlement-intents/idempotency/{idempotencyKey}")
    public SettlementIntent getByIdempotencyKey(@PathVariable String idempotencyKey) {
        return findExistingIntent(idempotencyKey);
    }

    @PostMapping("/settlement-intents/idempotency/{idempotencyKey}/confirming")
    public SettlementIntent markConfirming(@PathVariable String idempotencyKey) {
        return settlementService.markConfirming(findExistingIntent(idempotencyKey));
    }

    @PostMapping("/settlement-intents/idempotency/{idempotencyKey}/settled")
    public SettlementIntent markSettled(@PathVariable String idempotencyKey) {
        return settlementService.markSettled(findExistingIntent(idempotencyKey));
    }

    @PostMapping("/settlement-intents/idempotency/{idempotencyKey}/needs-review")
    public SettlementIntent markNeedsReview(@PathVariable String idempotencyKey) {
        return settlementService.markNeedsReview(findExistingIntent(idempotencyKey));
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidStateTransition(IllegalStateException exception) {
        return new ApiError(exception.getMessage());
    }

    private SettlementIntent findExistingIntent(String idempotencyKey) {
        return settlementService.findByIdempotencyKey(idempotencyKey)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Settlement intent not found"));
    }
}
