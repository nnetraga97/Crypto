package com.nnetraga.crypto.settlement.application;

public class IdempotencyKeyAlreadyExistsException extends RuntimeException {
    public IdempotencyKeyAlreadyExistsException(String message) {
        super(message);
    }
}
