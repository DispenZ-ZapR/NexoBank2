package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.Employee;
import com.example.nexobank2.entity.EmployeePosition;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.EmployeeStatus;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.EmployeePositionRepository;
import com.example.nexobank2.repository.EmployeeRepository;
import com.example.nexobank2.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeePositionRepository positionRepository;
    private final UserServiceImpl userService;
    private final EmailServiceImpl emailService;

    @Override
    public Employee getByUserEmail(String email) {
        return employeeRepository.findEmployeeByUserEmail(email).orElseThrow(() -> new NotFoundException("Employee not found"));
    }

    @Transactional
    @Override
    public void changeSalary(Long employeeId, BigDecimal newSalary) {
        Employee employee = findById(employeeId);
        employee.setSalary(newSalary);
        employeeRepository.save(employee);
    }

    @Override
    public Employee getUserById(Long id) {
        return employeeRepository.findByUserId(id).orElseThrow(() -> new NotFoundException("Employee not found"));
    }

    @Override
    public Employee getMyProfile(Long userId) {
        Employee employee = getUserById(userId);
        return findById(employee.getId());
    }

    @Override
    public List<Employee> getByEmployeeStatus(EmployeeStatus status) {
        return employeeRepository.findEmployeesByEmployeeStatus(status).orElse(List.of());
    }

    @Override
    public List<Employee> getEmployeeByPosition(Long id) {
        return employeeRepository.findByPositionsId(id).orElse(List.of());
    }

    @Transactional
    @Override
    public void changeEmployeeStatus(Long id, EmployeeStatus status) {
        Employee employee = findById(id);
        
        if (status == EmployeeStatus.ACTIVE) {
            employee.getUser().setDeletedAt(null);
        } else if (status == EmployeeStatus.FIRED && employee.getEmployeeStatus() != EmployeeStatus.FIRED) {
            employee.getUser().setDeletedAt(LocalDateTime.now());
        }
        
        employee.setEmployeeStatus(status);
        employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public void changeEmployeePosition(List<Long> newPositionIds, Long employeeId) {
        Employee employee = findById(employeeId);
        Set<EmployeePosition> newPositions = newPositionIds.stream()
                .map(id -> positionRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Позиция с ID " + id + " не найдена")))
                .collect(Collectors.toSet());

        employee.getPositions().clear();
        employee.getPositions().addAll(newPositions);
        employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public void addEmployeePosition(Long positionId, Long employeeId) {
        Employee employee = findById(employeeId);
        EmployeePosition position = positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("Позиция не найдена"));
        
        employee.getPositions().add(position);
        employeeRepository.save(employee);
    }

    @Override
    public Employee getByPassportFirstName(String name) {
        return employeeRepository.findByUser_Passport_FirstNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("Employee not found"));
    }

    @Transactional
    @Override
    public void removeEmployeePosition(Long positionId, Long employeeId) {
        Employee employee = findById(employeeId);
        EmployeePosition position = positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("Позиция не найдена"));

        employee.getPositions().remove(position);
        employeeRepository.save(employee);
    }
    @Transactional
    @Override
    public Employee save(Employee entity) {
        entity.setEmployeeStatus(EmployeeStatus.UNVERIFIED);
        User savedUser = userService.save(entity.getUser());
        
        Set<EmployeePosition> validatedPositions = entity.getPositions().stream()
                .map(position -> positionRepository.findById(position.getId())
                        .orElseThrow(() -> new NotFoundException("Позиция с ID " + position.getId() + " не найдена")))
                .collect(Collectors.toSet());
        
        entity.setPositions(validatedPositions);
        entity.setUser(savedUser);
        entity.setHiredAt(LocalDateTime.now());
        String link = "http://localhost:8080/api/user/verify/" + savedUser.getAcToken();
        emailService.sendSimpleMessage(savedUser.getEmail(),"Подтверждение аккаунта",
                "Для активации аккаунта перейдите по ссылке и установите пароль: " + link +
                        "\n\nПосле перехода по ссылке вам будет предложено установить пароль для входа в систему.");
        return employeeRepository.save(entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
       changeEmployeeStatus(id, EmployeeStatus.FIRED);
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("Сотрудник не найден"));
    }

    @Override
    public List<Employee> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return employees.getContent();
    }
}
