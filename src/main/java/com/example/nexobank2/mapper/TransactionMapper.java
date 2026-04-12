package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.TransactionResponse;
import com.example.nexobank2.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "transactionType", target = "transactionType")
    TransactionResponse toResponse(Transaction transaction);
}
