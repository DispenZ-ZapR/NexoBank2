package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Account;
import com.example.nexobank2.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClientId(Long clientId);
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<List<Account>> findByStatus(AccountStatus status);
    Optional<List<Account>> findByClientIdAndStatus(Long clientId, AccountStatus status);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByAccountType_Id(Long accountTypeId);
    List<Account> findByCurrency_Id(Long currencyId);
}
