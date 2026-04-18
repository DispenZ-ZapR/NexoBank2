package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.TransactionResponse;
import com.example.nexobank2.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    @Mapping(target = "accountId", expression = "java(transaction.getAccount() != null ? transaction.getAccount().getId() : null)")
    @Mapping(source = "transactionType", target = "transactionType")
    TransactionResponse toResponse(Transaction transaction);
    
    List<TransactionResponse> toResponseList(List<Transaction> transactions);
}
