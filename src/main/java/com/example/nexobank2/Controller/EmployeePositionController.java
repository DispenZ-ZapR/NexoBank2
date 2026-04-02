package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.EmployeePositionRequest;
import com.example.nexobank2.dto.EmployeePositionResponse;
import com.example.nexobank2.entity.EmployeePosition;
import com.example.nexobank2.mapper.EmployeePositionMapper;
import com.example.nexobank2.service.impl.PositionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-position")
@AllArgsConstructor
@Tag(name = "Контроллер позиций сотрудника")
public class EmployeePositionController {
    private final PositionService service;
    private final EmployeePositionMapper mapper;
    @PostMapping("/save")
    public ResponseEntity<EmployeePositionResponse> save(@RequestBody EmployeePositionRequest request){
        return ResponseEntity.ok(mapper.toResponse(service.save(mapper.toEntity(request))));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.ok("Должность удалена");
    }

    @GetMapping("/getByid/{id}")
    public ResponseEntity<EmployeePositionResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeePositionResponse>> get() {
        return ResponseEntity.ok(mapper.toResponseList(service.findAll()));
    }
}
