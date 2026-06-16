package com.example.nexobank2.service;

import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.Client;
import com.example.nexobank2.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService extends BaseService<Account>{
    Account createDefaultAccount(Client client);
    
    void activateClientAccounts(Long clientId);
    
    List<Account> findByClientId(Long clientId);

    Account findByAccountNumber(String accountNumber);

    List<Account> findByStatus(AccountStatus status);

    List<Account> findByClientIdAndStatus(Long clientId, AccountStatus status);

    void changeAccountStatus(Long accountId, AccountStatus status);

    void unblockAccount(Long accountId);

    void freezeAccount(Long accountId);

    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);

    BigDecimal getBalance(Long accountId);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findActiveAccountsByClientId(Long clientId);

    List<Account> findByCurrencyId(Long currencyId);

    List<Account> findByAccountTypeId(Long accountTypeId);

}
