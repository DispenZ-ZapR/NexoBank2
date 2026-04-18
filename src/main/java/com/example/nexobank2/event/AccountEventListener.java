package com.example.nexobank2.event;

import com.example.nexobank2.entity.Client;
import com.example.nexobank2.service.AccountService;
import com.example.nexobank2.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AccountEventListener {
    private final AccountService accountService;
    private final ClientService clientService;

    @EventListener
    @Transactional
    public void onClientCreated(ClientCreatedEvent event) {
        Client client = clientService.findById(event.getClientId());
        accountService.createDefaultAccount(client);
    }

    @EventListener
    @Transactional
    public void onClientActivated(ClientActivatedEvent event) {
        accountService.activateClientAccounts(event.getClientId());
    }
}
