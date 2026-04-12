package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOperationId(UUID operationId);
}
