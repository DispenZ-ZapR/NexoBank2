package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.OperationRequest;
import com.example.nexobank2.dto.OperationResponse;
import com.example.nexobank2.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OperationMapper {
    Operation toEntity(OperationRequest operationRequest);
    @Mapping(target = "initiatorId", source = "initiator.id")
    OperationResponse toResponse(Operation operation);
}
