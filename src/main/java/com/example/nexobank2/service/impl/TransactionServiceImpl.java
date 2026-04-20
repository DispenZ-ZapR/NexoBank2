package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.Transaction;
import com.example.nexobank2.enums.TransactionType;
import com.example.nexobank2.exception.ForbiddenException;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.AccountRepository;
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
    private final AccountRepository accountRepository;
    
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
        return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Транзакция не найдена"));
    }

    @Override
    public List<Transaction> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.getContent();
    }

    @Override
    public List<Transaction> findByOperationId(UUID operationId) {
        return transactionRepository.findByOperationId(operationId);
    }
    
    @Override
    public List<Transaction> getMyTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }
    
    @Override
    public List<Transaction> getAccountTransactions(Long accountId, Long userId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new NotFoundException("Счет не найден"));
        
        if (!account.getClient().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Это не ваш счет!");
        }
        
        return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId);
    }

}
