package com.example.nexobank2.controller;

import com.example.nexobank2.dto.AccountTypeRequest;
import com.example.nexobank2.mapper.AccountTypeMapper;
import com.example.nexobank2.service.AccountTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account-type")
@AllArgsConstructor
@Tag(name = "Типы счетов", description = "Управление типами банковских счетов")
public class AccountTypeController {
    private final AccountTypeService service;
    private final AccountTypeMapper mapper;
    @PostMapping("/save")
    @Operation(summary = "Создать тип счета", description = "Создание нового типа банковского счета")
    public ResponseEntity<?> save(@RequestBody AccountTypeRequest request) {
        service.save(mapper.toEntity(request));
        return ResponseEntity.ok("Тип счета создан!");
    }

    @GetMapping("/getAll")
    @Operation(summary = "Получить список типов счетов", description = "Получение списка всех типов счетов с пагинацией")
    public ResponseEntity<?> getAll(
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(mapper.toResponseList(service.findAll(page,size)));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить тип счета", description = "Удаление типа счета по идентификатору")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Тип счета удален!");
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Получить тип счета по ID", description = "Получение информации о типе счета по идентификатору")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }
}
