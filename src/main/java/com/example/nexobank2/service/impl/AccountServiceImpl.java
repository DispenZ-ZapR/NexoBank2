package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.AccountCurrency;
import com.example.nexobank2.entity.AccountRequest;
import com.example.nexobank2.entity.AccountType;
import com.example.nexobank2.entity.Client;
import com.example.nexobank2.enums.AccountStatus;
import com.example.nexobank2.exception.BadRequestException;
import com.example.nexobank2.exception.BaseException;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.AccountCurrencyRepository;
import com.example.nexobank2.repository.AccountRepository;
import com.example.nexobank2.repository.AccountRequestRepository;
import com.example.nexobank2.repository.AccountTypeRepository;
import com.example.nexobank2.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountCurrencyRepository accountCurrencyRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    @Override
    public List<Account> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.getContent();
    }
    @Override
    @Named("findByIdAc")
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(()-> new NotFoundException("Счет не найден"));
    }

    @Override
    public void deleteById(Long id) {
        Account account = findById(id);
        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account);
    }

    @Override
    public Account save(Account entity) {
        return accountRepository.save(entity);
    }

    @Override
    public List<Account> findByClientId(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new NotFoundException("Счет не найден"));
    }

    @Override
    public List<Account> findByStatus(AccountStatus status) {
        return accountRepository.findByStatus(status).orElseThrow(() -> new NotFoundException("Счета не найдены"));
    }

    @Override
    public List<Account> findByClientIdAndStatus(Long clientId, AccountStatus status) {
        return accountRepository.findByClientIdAndStatus(clientId, status).orElseThrow(() -> new NotFoundException("Счета не найдены"));
    }

    @Override
    public void changeAccountStatus(Long accountId, AccountStatus status) {
        Account account = findById(accountId);
        account.setStatus(status);
        accountRepository.save(account);
    }

    @Override
    public void unblockAccount(Long accountId) {
        Account account = findById(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    @Override
    public void freezeAccount(Long accountId) {
        Account account = findById(accountId);
        account.setStatus(AccountStatus.FREEZE);
        accountRepository.save(account);
    }
    @Override
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Сумма должна быть больше 0");
        }
        
        Account fromAccount = findById(fromAccountId);
        Account toAccount = findById(toAccountId);
        
        if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Счет отправителя не активен");
        }
        if (toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Счет получателя не активен");
        }
        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new BadRequestException("Нельзя перевести деньги самому себе");
        }
        if (!fromAccount.getCurrency().getId().equals(toAccount.getCurrency().getId())) {
            throw new BadRequestException(
                String.format("Перевод возможен только между счетами в одной валюте. Счет отправителя: %s, счет получателя: %s",
                    fromAccount.getCurrency().getCode(),

                    toAccount.getCurrency().getCode())
            );
        }
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Недостаточно средств");
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        Account account = findById(accountId);
        return account.getBalance();
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> findActiveAccountsByClientId(Long clientId) {
        return accountRepository.findByClientId(clientId).stream().filter(account -> account.getStatus() == AccountStatus.ACTIVE).toList();
    }

    @Override
    public List<Account> findByCurrencyId(Long currencyId) {
        return accountRepository.findByCurrency_Id(currencyId);
    }

    @Override
    @Transactional
    public Account createDefaultAccount(Client client) {
        AccountType defaultType = accountTypeRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Базовый тип счета не найден"));
        
        AccountCurrency defaultCurrency = accountCurrencyRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Базовая валюта не найдена"));
        
        Account account = new Account();
        account.setClient(client);
        account.setAccountType(defaultType);
        account.setCurrency(defaultCurrency);
        account.setStatus(AccountStatus.UNVERIFIED);
        account.setBalance(BigDecimal.ZERO);
        account.setDateCreated(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        String accountNumber;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10;
        
        do {
            long timestamp = System.currentTimeMillis();
            int randomPart = secureRandom.nextInt(10000); // 0-9999
            accountNumber = String.format("KG%d%04d", timestamp, randomPart);
            
            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                log.error("Не удалось сгенерировать уникальный номер счета после {} попыток", MAX_ATTEMPTS);
                throw new RuntimeException("Не удалось сгенерировать уникальный номер счета");
            }
        } while (existsByAccountNumber(accountNumber));
        
        log.debug("Сгенерирован номер счета: {} (попыток: {})", accountNumber, attempts);
        return accountNumber;
    }

    @Override
    public List<Account> findByAccountTypeId(Long accountTypeId) {
        return accountRepository.findByAccountType_Id(accountTypeId);
    }

    @Override
    @Transactional
    public void activateClientAccounts(Long clientId) {
        List<Account> accounts = findByClientId(clientId);
        for (Account account : accounts) {
            if (account.getStatus() == AccountStatus.UNVERIFIED) {
                account.setStatus(AccountStatus.ACTIVE);
                accountRepository.save(account);
            }
        }}
}

