package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.BaseEntity;
import com.example.nexobank2.entity.EmployeePosition;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.EmployeePositionRepository;
import com.example.nexobank2.service.EmployeePositionService;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PositionService implements EmployeePositionService  {
    private final EmployeePositionRepository repository;

    @Override
    public EmployeePosition save(EmployeePosition entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Named("findByIdP")
    public EmployeePosition findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Position not found"));
    }

    @Override
    public List<EmployeePosition> findAll() {
        return repository.findAll();
    }
}
