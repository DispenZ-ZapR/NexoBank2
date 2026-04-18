package com.example.nexobank2.service;

import com.example.nexobank2.entity.Employee;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.EmployeeStatus;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    Employee getByPassportFirstName(String name);
    Employee getByUserEmail(String email);
    Employee getUserById(Long id);

    List<Employee> getByEmployeeStatus(EmployeeStatus status);
    List<Employee> getEmployeeByPosition(Long id);

    void changeEmployeeStatus(Long id, EmployeeStatus status);
    void changeEmployeePosition(List<Long> newPositionIds, Long employeeId);
    void addEmployeePosition(Long positionId, Long employeeId);
    void removeEmployeePosition(Long positionId, Long employeeId);
    void changeSalary(Long employeeId, BigDecimal newSalary);
    Employee save(Employee entity);
    void deleteById(Long id);
    Employee findById(Long id);
    List<Employee> findAll();

    Employee getMyProfile(Long userId);
}

