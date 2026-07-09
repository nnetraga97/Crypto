package com.nnetraga.crypto.settlement.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringSettlementEventPublisher implements SettlementEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringSettlementEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(SettlementStateChangedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
