package com.example.nexobank2.controller;

import com.example.nexobank2.dto.EmployeeRequest;
import com.example.nexobank2.dto.EmployeeResponse;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.EmployeeStatus;
import com.example.nexobank2.mapper.EmployeeMapper;
import com.example.nexobank2.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Работники")
@AllArgsConstructor
@Validated
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @PostMapping("/save")
    public ResponseEntity<EmployeeResponse> save(@RequestBody @Valid EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.save(employeeMapper.toEntity(employeeRequest))));
    }
    @PutMapping("/changeSalary/{id}")
    public ResponseEntity<?> changeSalary(@PathVariable @Min(1) Long id,@RequestParam
    @NotNull(message = "Зарплата обязательна!")
    @DecimalMin(value = "0.01", message = "Зарплата должна быть больше 0")
    @Digits(integer = 19, fraction = 4, message = "Некорректный формат зарплаты") BigDecimal newSalary) {
        employeeService.changeSalary(id, newSalary);
        return ResponseEntity.ok("Зарплата изменена!");
    }
    @DeleteMapping("/FireEmployee/{id}")
    public ResponseEntity<?> fireEmployee(@PathVariable @Min(1) Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok("Сотрудник уволен!");
    }
    @GetMapping("getById/{id}")
    public ResponseEntity<?> getById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.findById(id)));
    }
    @GetMapping("/getByFirstName")
    public ResponseEntity<EmployeeResponse> getByFirstName(@RequestParam @NotBlank String firstName) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getByPassportFirstName(firstName)));
    }
    @GetMapping("/getByEmail")
    public ResponseEntity<EmployeeResponse> getByEmail(@RequestParam @Email String email) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getByUserEmail(email)));
    }
    @GetMapping("/getuserById")
    public ResponseEntity<EmployeeResponse> getUserById(@RequestParam @Min(1) Long id) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getUserById(id)));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.findAll(page,size)));
    }
    @GetMapping("/getByEmployeeStatus")
    public ResponseEntity<List<EmployeeResponse>> getByEmployeeStatus(@RequestParam @NotNull EmployeeStatus employeeStatus) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.getByEmployeeStatus(employeeStatus)));
    }
    @GetMapping("/getByPosition/{id}")
    public ResponseEntity<List<EmployeeResponse>> getByPosition(@PathVariable(name = "id") Long positionId) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.getEmployeeByPosition(positionId)));
    }
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable @Min(1) Long id, @RequestParam @NotNull EmployeeStatus status){
        employeeService.changeEmployeeStatus(id, status);
        return ResponseEntity.ok("Статус сотрудника изменен!");
    }
    @PutMapping("/changePosition/{employeeId}")
    public ResponseEntity<?> changePosition(@RequestParam
                                                @NotEmpty(message = "Список должностей не может быть пустым")
                                                @Size(min = 1, message = "Должна быть хотя бы одна должность")
                                                List<@Min(value = 1, message = "ID должности должен быть положительным") Long> positionIds, @PathVariable @Min(1) Long employeeId){
        employeeService.changeEmployeePosition(positionIds, employeeId);
        return ResponseEntity.ok("Позиция сотрудника изменена!");
    }
    @PostMapping("/addPosition/{positionId}/{employeeId}")
    public ResponseEntity<?> addPosition(@PathVariable @Min(1) Long positionId, @PathVariable @Min(1) Long employeeId){
        employeeService.addEmployeePosition(positionId, employeeId);
        return ResponseEntity.ok("Должность добавлена!");
    }
    @DeleteMapping("/removePosition/{positionId}/{employeeId}")
    public ResponseEntity<?> removePosition(@PathVariable @Min(1) Long positionId, @PathVariable @Min(1) Long employeeId){
        employeeService.removeEmployeePosition(positionId, employeeId);
        return ResponseEntity.ok("Должность удалена!");
    }

    @GetMapping("/myProfile")
    public ResponseEntity<EmployeeResponse> myProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getMyProfile(user.getId())));
    }
}
