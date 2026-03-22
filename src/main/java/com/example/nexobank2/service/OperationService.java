package com.example.nexobank2.service;

import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.repository.OperationRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    @Named("getByUUID")
    public Operation getByUUID(UUID id){
        return operationRepository.findById(id).orElseThrow(()-> new RuntimeException("Operation not found"));
    }
}
