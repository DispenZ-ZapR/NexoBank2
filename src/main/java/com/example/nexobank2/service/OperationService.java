package com.example.nexobank2.service;

import com.example.nexobank2.dto.TransactionRequest;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.Transaction;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.OperationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OperationService {
    Operation transfer (TransactionRequest request, User initiator);
    Operation getByUUID (UUID uuid);
    List<Operation> getByAll();
    List<Operation> findByInitiatorId(Long id);
    List<Operation> findByStatus(OperationStatus status);
    List<Operation> getMyOperations(Long userId);
}
