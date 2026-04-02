package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.EmployeeRequest;
import com.example.nexobank2.dto.EmployeeResponse;
import com.example.nexobank2.entity.Employee;
import com.example.nexobank2.enums.EmployeeStatus;
import com.example.nexobank2.mapper.EmployeeMapper;
import com.example.nexobank2.service.EmployeeService;
import com.example.nexobank2.service.impl.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Работники")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;
    private final EmployeeMapper employeeMapper;

    @PostMapping("/save")
    public ResponseEntity<EmployeeResponse> save(@RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.save(employeeMapper.toEntity(employeeRequest))));
    }
    @PutMapping("/changeSalary/{id}")
    public ResponseEntity<?> changeSalary(@PathVariable Long id, @RequestParam BigDecimal newSalary) {
        employeeService.changeSalary(id, newSalary);
        return ResponseEntity.ok("Зарплата изменена!");
    }
    @DeleteMapping("/FireEmployee/{id}")
    public ResponseEntity<?> fireEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok("Сотрудник уволен!");
    }
    @GetMapping("getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.findById(id)));
    }
    @GetMapping("/getByFirstName")
    public ResponseEntity<EmployeeResponse> getByFirstName(@RequestParam String firstName) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getByPassportFirstName(firstName)));
    }
    @GetMapping("/getByEmail")
    public ResponseEntity<EmployeeResponse> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getByUserEmail(email)));
    }
    @GetMapping("/getuserById")
    public ResponseEntity<EmployeeResponse> getUserById(@RequestParam Long id) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getUserById(id)));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.findAll()));
    }
    @GetMapping("/getByEmployeeStatus")
    public ResponseEntity<List<EmployeeResponse>> getByEmployeeStatus(@RequestParam EmployeeStatus employeeStatus) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.getByEmployeeStatus(employeeStatus)));
    }
    @GetMapping("/getByPosition")
    public ResponseEntity<List<EmployeeResponse>> getByPosition(@RequestParam Long positionId) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.getEmployeeByPosition(positionId)));
    }
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestParam EmployeeStatus status){
        employeeService.changeEmployeeStatus(id, status);
        return ResponseEntity.ok("Статус сотрудника изменен!");
    }
    @PutMapping("/changePosition/{employeeId}")
    public ResponseEntity<?> changePosition(@RequestParam List<Long> positionIds, @PathVariable Long employeeId){
        employeeService.changeEmployeePosition(positionIds, employeeId);
        return ResponseEntity.ok("Позиция сотрудника изменена!");
    }
    @PostMapping("/addPosition/{positionId}/{employeeId}")
    public ResponseEntity<?> addPosition(@PathVariable Long positionId, @PathVariable Long employeeId){
        employeeService.addEmployeePosition(positionId, employeeId);
        return ResponseEntity.ok("Должность добавлена!");
    }
    @DeleteMapping("/removePosition/{positionId}/{employeeId}")
    public ResponseEntity<?> removePosition(@PathVariable Long positionId, @PathVariable Long employeeId){
        employeeService.removeEmployeePosition(positionId, employeeId);
        return ResponseEntity.ok("Должность удалена!");
    }
}
