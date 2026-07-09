package com.nnetraga.crypto.settlement.application;

public class IdempotencyRequestInProgressException extends RuntimeException {
    public IdempotencyRequestInProgressException(String message) {
        super(message);
    }
}
