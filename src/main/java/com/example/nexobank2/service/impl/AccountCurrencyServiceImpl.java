package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.AccountCurrency;
import com.example.nexobank2.entity.AccountRequest;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.AccountCurrencyRepository;
import com.example.nexobank2.service.AccountCurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AccountCurrencyServiceImpl implements AccountCurrencyService {
    private final AccountCurrencyRepository accountCurrencyRepository;
    @Override
    public AccountCurrency save(AccountCurrency entity) {
        return accountCurrencyRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        accountCurrencyRepository.deleteById(id);
    }

    @Override
    public AccountCurrency findById(Long id) {
        return accountCurrencyRepository.findById(id).orElseThrow(()-> new NotFoundException("Not found"));
    }

    @Override
    public List<AccountCurrency> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<AccountCurrency> accountRequests = accountCurrencyRepository.findAll(pageable);
        return accountRequests.getContent();
    }
}
