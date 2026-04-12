package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.OperationRequest;
import com.example.nexobank2.dto.OperationResponse;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperationMapper {
    @Mapping(source = "operation.initiator.id", target = "initiatorId")
    @Mapping(source = "operation.status", target = "status")
    @Mapping(source = "operation.reason", target = "reason")
    @Mapping(source = "operation.channel", target = "channel")
    @Mapping(source = "operation.createdAt", target = "createdAt")
    @Mapping(source = "transactions", target = "transactions")
    OperationResponse toResponse(Operation operation, List<Transaction> transactions);

    List<OperationResponse> toResponseList(List<Operation> operations);

}
