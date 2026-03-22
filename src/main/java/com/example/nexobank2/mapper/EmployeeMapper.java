package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.EmployeeRequest;
import com.example.nexobank2.dto.EmployeeResponse;
import com.example.nexobank2.entity.Employee;
import com.example.nexobank2.service.PositionService;
import com.example.nexobank2.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {PositionService.class, UserService.class})
public interface EmployeeMapper {
    @Mapping(target = "position", source = "positionId", qualifiedByName = "findByIdP")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "user", source = "userId", qualifiedByName = "findById")
    @Mapping(target = "hiredAt", ignore = true)
    @Mapping(target = "employeeStatus", ignore = true)
    Employee toEntity(EmployeeRequest employeeRequest);
    
    @Mapping(target = "position", source = "position.name")
    @Mapping(target = "userId", source = "user.id")
    EmployeeResponse toResponse(Employee employee);
}
