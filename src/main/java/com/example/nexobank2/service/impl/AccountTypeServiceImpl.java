package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.AccountRequest;
import com.example.nexobank2.entity.AccountType;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.AccountTypeRepository;
import com.example.nexobank2.service.AccountTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {
    private final AccountTypeRepository repository;
    @Override
    public AccountType save(AccountType entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public AccountType findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Не найдено"));
    }

    @Override
    public List<AccountType> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<AccountType> accountRequests = repository.findAll(pageable);
        return accountRequests.getContent();
    }
}
