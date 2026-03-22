package com.example.nexobank2.service;

import com.example.nexobank2.entity.EmployeePosition;
import com.example.nexobank2.repository.EmployeePositionRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PositionService {
    private final EmployeePositionRepository employeePositionRepository;
    @Named("findByIdP")
    public EmployeePosition findByIdP(Long id){
        return employeePositionRepository.findById(id).orElseThrow(()-> new RuntimeException("not found"));
    }
}
