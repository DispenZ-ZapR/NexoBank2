package com.example.nexobank2.service;


import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService extends BaseService<Transaction>{
    void record(Account fromAccount, Account toAccount, BigDecimal amount, Operation operation);
    List<Transaction> findByOperationId(UUID operationId);
    List<Transaction> getMyTransactions(Long userId);
    List<Transaction> getAccountTransactions(Long accountId, Long userId);
}
