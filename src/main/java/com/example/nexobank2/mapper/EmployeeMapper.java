package com.example.nexobank2.mapper;

import com.example.nexobank2.dto.EmployeeRequest;
import com.example.nexobank2.dto.EmployeeResponse;
import com.example.nexobank2.entity.Employee;
import com.example.nexobank2.entity.EmployeePosition;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.entity.Passport;
import com.example.nexobank2.enums.UserType;
import com.example.nexobank2.service.impl.PositionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {PositionService.class})
public abstract class EmployeeMapper {
    
    @Autowired
    protected PositionService positionService;
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "positions", source = "positionId", qualifiedByName = "positionIdToPositions")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "hiredAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employeeStatus", constant = "ACTIVE")
    @Mapping(target = "user", source = "employeeRequest", qualifiedByName = "requestToUser")
    public abstract Employee toEntity(EmployeeRequest employeeRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "deletedAt", source = "user.deletedAt")
    @Mapping(target = "firstName", source = "user.passport.firstName")
    @Mapping(target = "lastName", source = "user.passport.lastName")
    @Mapping(target = "middleName", source = "user.passport.middleName")
    @Mapping(target = "positions", source = "positions", qualifiedByName = "positionsToPositionNames")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "hiredAt", source = "hiredAt")
    @Mapping(target = "employeeStatus", source = "employeeStatus", qualifiedByName = "employeeStatusToString")
    public abstract EmployeeResponse toResponse(Employee employee);
    
    public abstract List<EmployeeResponse> toResponseList(List<Employee> employees);

    @Named("positionIdToPositions")
    protected Set<EmployeePosition> positionIdToPositions(Long positionId) {
        if (positionId == null) {
            return Collections.emptySet();
        }
        EmployeePosition position = new EmployeePosition();
        position.setId(positionId);
        return Collections.singleton(position);
    }
    
    @Named("positionsToPositionNames")
    protected List<String> positionsToPositionNames(Set<EmployeePosition> positions) {
        if (positions == null || positions.isEmpty()) {
            return Collections.emptyList();
        }
        return positions.stream()
                .map(EmployeePosition::getName)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Named("employeeStatusToString")
    protected String employeeStatusToString(com.example.nexobank2.enums.EmployeeStatus status) {
        return status != null ? status.name() : null;
    }
    
    @Named("requestToUser")
    protected User requestToUser(EmployeeRequest employeeRequest) {
        if (employeeRequest == null) {
            return null;
        }
        
        User user = new User();
        user.setEmail(employeeRequest.getEmail());
        user.setPhoneNumber(employeeRequest.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());
        user.setUserType(UserType.EMPLOYEE);
        
        Passport passport = new Passport();
        passport.setFirstName(employeeRequest.getFirstName());
        passport.setLastName(employeeRequest.getLastName());
        passport.setMiddleName(employeeRequest.getMiddleName());
        passport.setDateOfBirth(employeeRequest.getDateOfBirth());
        passport.setPersonalNumber(employeeRequest.getPersonalNumber());
        passport.setPassportNumber(employeeRequest.getPassportNumber());
        
        user.setPassport(passport);
        return user;
    }
}
