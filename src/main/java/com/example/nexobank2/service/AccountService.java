package com.example.nexobank2.service;

import com.example.nexobank2.entity.Account;
import com.example.nexobank2.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    @Named("findByIdAc")
    public Account getById(Long id){
        return accountRepository.findById(id).orElseThrow(()-> new RuntimeException("not found"));
    }
}
