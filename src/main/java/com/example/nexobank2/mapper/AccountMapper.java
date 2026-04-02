package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.AccountRequest;
import com.example.nexobank2.dto.AccountResponse;
import com.example.nexobank2.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "accountType.id", source = "accountTypeId")
    @Mapping(target = "currency.id", source = "currencyId")
    @Mapping(target = "balance", constant = "0")
    @Mapping(target = "dateCreated", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "ACTIVE")
    Account toEntity(AccountRequest request);
    
    @Mapping(target = "createdAt", source = "dateCreated")
    @Mapping(target = "accountType", source = "accountType.name")
    @Mapping(target = "currencyName", source = "currency.name")
    @Mapping(target = "status", source = "status")
    AccountResponse toResponse(Account account);
    
    List<AccountResponse> toResponseList(List<Account> accounts);
}
