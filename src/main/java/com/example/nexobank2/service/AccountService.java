package com.example.nexobank2.service;

import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.Client;
import com.example.nexobank2.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService extends BaseService<Account>{
    // Создание базового счета при регистрации
    Account createDefaultAccount(Client client);
    
    // Активация счетов клиента после верификации
    void activateClientAccounts(Long clientId);
    
    List<Account> findByClientId(Long clientId);

    Account findByAccountNumber(String accountNumber);

    List<Account> findByStatus(AccountStatus status);

    List<Account> findByClientIdAndStatus(Long clientId, AccountStatus status);

    void changeAccountStatus(Long accountId, AccountStatus status);

    void unblockAccount(Long accountId);

    void freezeAccount(Long accountId);

    // Перевод между счетами
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);

    // Получить баланс
    BigDecimal getBalance(Long accountId);

    // Проверка существования номера счета
    boolean existsByAccountNumber(String accountNumber);

    // Поиск активных счетов клиента
    List<Account> findActiveAccountsByClientId(Long clientId);

    // Поиск счетов по типу валюты
    List<Account> findByCurrencyId(Long currencyId);

    // Поиск счетов по типу счета
    List<Account> findByAccountTypeId(Long accountTypeId);

}
