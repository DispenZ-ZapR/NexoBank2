package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.EmployeePositionRequest;
import com.example.nexobank2.dto.EmployeePositionResponse;
import com.example.nexobank2.entity.EmployeePosition;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeePositionMapper {
    EmployeePosition toEntity(EmployeePositionRequest request);
    EmployeePositionResponse toResponse(EmployeePosition employeePosition);
    List<EmployeePositionResponse> toResponseList(List<EmployeePosition> positions);
}
