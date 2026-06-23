package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.AccountCurrencyRequest;
import com.example.nexobank2.dto.AccountCurrencyResponse;
import com.example.nexobank2.entity.AccountCurrency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountCurrencyMapper {
    @Mapping(target = "id", ignore = true)
    AccountCurrency toEntity(AccountCurrencyRequest request);
    AccountCurrencyResponse toResponse(AccountCurrency accountCurrency);
    List<AccountCurrency> toResponseList(List<AccountCurrency> accountCurrencies);
}
