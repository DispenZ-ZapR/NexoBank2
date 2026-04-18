package com.example.nexobank2.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ClientCreatedEvent extends ApplicationEvent {
    private final Long clientId;

    public ClientCreatedEvent(Object source, Long clientId) {
        super(source);
        this.clientId = clientId;
    }
}
