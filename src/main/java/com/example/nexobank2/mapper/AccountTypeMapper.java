package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.AccountTypeRequest;
import com.example.nexobank2.dto.AccountTypeResponse;
import com.example.nexobank2.entity.AccountType;
import org.mapstruct.Mapper;

@Mapper
public interface AccountTypeMapper {
    AccountType toEntity(AccountTypeRequest request);
    AccountTypeResponse toResponse(AccountType accountType);
}
