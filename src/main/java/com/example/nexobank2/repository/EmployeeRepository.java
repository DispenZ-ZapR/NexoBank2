package com.example.nexobank2.repository;

import com.example.nexobank2.entity.Employee;
import com.example.nexobank2.entity.EmployeePosition;
import com.example.nexobank2.enums.EmployeeStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findEmployeeByUserEmail(String userEmail);
    Optional<Employee> findByUser_Passport_FirstNameIgnoreCase(String name);
    Optional<Employee> findByUserId(Long id);

    Optional<List<Employee>> findEmployeesByEmployeeStatus(EmployeeStatus status);
    Optional<List<Employee>> findByPositionsId(Long id);
}
