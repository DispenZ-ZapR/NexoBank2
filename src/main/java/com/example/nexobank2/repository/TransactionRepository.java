package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t from Transaction t where t.account.client.user.id = :userId")
    List<Transaction> findByUserId(@Param("userId") Long userId);

    List<Transaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);
    List<Transaction> findByOperationId(UUID operationId);
}
