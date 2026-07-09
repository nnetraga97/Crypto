package com.nnetraga.crypto.settlement.application;

public interface SettlementEventPublisher {
    void publish(SettlementStateChangedEvent event);
}
