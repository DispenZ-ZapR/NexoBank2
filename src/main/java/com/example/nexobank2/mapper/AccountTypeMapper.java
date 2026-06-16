package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.AccountTypeRequest;
import com.example.nexobank2.dto.AccountTypeResponse;
import com.example.nexobank2.entity.AccountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountTypeMapper {
    @Mapping(target = "id", ignore = true)
    AccountType toEntity(AccountTypeRequest request);
    AccountTypeResponse toResponse(AccountType accountType);
}
