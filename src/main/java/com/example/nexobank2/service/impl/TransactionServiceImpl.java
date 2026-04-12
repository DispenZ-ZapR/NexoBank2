package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.Transaction;
import com.example.nexobank2.enums.TransactionType;
import com.example.nexobank2.repository.TransactionRepository;
import com.example.nexobank2.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    @Override
    public void record(Account fromAccount, Account toAccount, BigDecimal amount, Operation operation) {
        Transaction debit = new Transaction();
        debit.setAccount(fromAccount);
        debit.setAmount(amount.negate());
        debit.setOperation(operation);
        debit.setTransactionType(TransactionType.DEBIT);
        debit.setTransactionDate(LocalDateTime.now());
        debit.setTransactionAfter(fromAccount.getBalance());
        save(debit);

        Transaction credit = new Transaction();
        credit.setAccount(toAccount);
        credit.setAmount(amount);
        credit.setOperation(operation);
        credit.setTransactionType(TransactionType.CREDIT);
        credit.setTransactionDate(LocalDateTime.now());
        credit.setTransactionAfter(toAccount.getBalance());
        save(credit);
    }

    @Override
    public Transaction save(Transaction entity) {
        return transactionRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> findByOperationId(UUID operationId) {
        return transactionRepository.findByOperationId(operationId);
    }
}
