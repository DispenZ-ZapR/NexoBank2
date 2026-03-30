package com.example.nexobank2.service;

import com.example.nexobank2.entity.Client;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.ClientStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAll(ClientStatus status);
    Client save(User entity);
    Client findById(Long id);
    void deleteById(Long id);
    
    List<Client> findByCreatedAt(LocalDateTime createdAt);
    Optional<Client> findByUserId(Long userId);
    
   List<Client> findByDateRange(ClientStatus status, LocalDateTime startDate, LocalDateTime endDate);
    void updateClientStatus(Long id, ClientStatus status);
    Integer getClientCountByStatus(ClientStatus status);
}
