package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.AccountCurrencyRequest;
import com.example.nexobank2.dto.AccountCurrencyResponse;
import com.example.nexobank2.entity.AccountCurrency;
import org.mapstruct.Mapper;

@Mapper
public interface AccountCurrencyMapper {
    AccountCurrency toEntity(AccountCurrencyRequest request);
    AccountCurrencyResponse toResponse(AccountCurrency accountCurrency);
}
