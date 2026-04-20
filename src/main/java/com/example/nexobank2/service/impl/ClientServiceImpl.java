package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.Client;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.ClientStatus;
import com.example.nexobank2.enums.UserType;
import com.example.nexobank2.event.ClientCreatedEvent;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.ClientRepository;
import com.example.nexobank2.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final PassportServiceImpl passportService;
    private final UserServiceImpl userService;
    private final EmailServiceImpl emailService;
    private final ApplicationEventPublisher eventPublisher;
@Override
public List<Client> findAll(ClientStatus status, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
    Page<Client> clients = clientRepository.findByClientStatus(status, pageable);
    return clients.getContent();
}
    @Override
    @Transactional
    public Client save(Client entity) {
        entity.getUser().setUserType(UserType.CLIENT);
        User savedUser = userService.save(entity.getUser());
        Client client = new Client();
        client.setUser(savedUser);
        client.setCreditRating(0);
        client.setCreatedAt(LocalDateTime.now());
        client.setClientStatus(ClientStatus.UNVERIFIED);
        
        Client savedClient = clientRepository.save(client);
        
        eventPublisher.publishEvent(new ClientCreatedEvent(this, savedClient.getId()));
        
        String link = "http://localhost:8080/api/user/verify/" + savedUser.getAcToken();
        emailService.sendSimpleMessage(savedUser.getEmail(),"Подтверждение аккаунта",
                "Для активации аккаунта перейдите по ссылке и установите пароль: " + link +
                        "\n\nПосле перехода по ссылке вам будет предложено установить пароль для входа в систему.");
        return savedClient;

    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(()-> new NotFoundException("Client not found"));
    }

    @Override
    public Client getMyProfile(Long userId) {
        return findByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        Client client =findById(id);
        client.setClientStatus(ClientStatus.DELETED);
        userService.delete(client.getUser());
        clientRepository.save(client);

    }

    @Override
    public List<Client> findByCreatedAt(LocalDateTime createdAt) {
        return clientRepository.findByCreatedAt(createdAt);
    }

    @Override
    public Client findByUserId(Long userId) {
        return clientRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Клиент не найден!"));
    }
    @Override
    public List<Client> findByDateRange(ClientStatus status,LocalDateTime startDate, LocalDateTime endDate) {
        return clientRepository.findByClientStatusAndCreatedAtBetween(status,startDate,endDate);
    }

    @Override
    public void updateClientStatus(Long id, ClientStatus status) {
        Client client = findById(id);
        client.setClientStatus(status);
        clientRepository.save(client);
    }

    @Override
    public Integer getClientCountByStatus(ClientStatus status) {
        return clientRepository.countByClientStatus(status);
    }
}
