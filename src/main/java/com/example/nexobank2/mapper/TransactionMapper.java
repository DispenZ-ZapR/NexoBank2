package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.TransactionRequest;
import com.example.nexobank2.dto.TransactionResponse;
import com.example.nexobank2.entity.Transaction;
import com.example.nexobank2.service.impl.AccountServiceImpl;
import com.example.nexobank2.service.impl.OperationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AccountServiceImpl.class, OperationService.class})
public interface TransactionMapper {
    @Mapping(target = "account", source = "accountId", qualifiedByName = "findByIdAc")
    @Mapping(target = "operation", source = "operationId", qualifiedByName = "getByUUID")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(target = "transactionAfter", ignore = true)
    Transaction toEntity(TransactionRequest transactionRequest);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "transactionDate", source = "transactionDate")
    @Mapping(target = "transactionAfter", source = "transactionAfter")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "operationId", source = "operation.id")
    @Mapping(target = "amount", source = "amount")
    TransactionResponse toResponse(Transaction transaction);
}
