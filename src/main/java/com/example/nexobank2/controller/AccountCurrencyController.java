package com.example.nexobank2.controller;

import com.example.nexobank2.dto.AccountCurrencyRequest;
import com.example.nexobank2.mapper.AccountCurrencyMapper;
import com.example.nexobank2.service.AccountCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Tag(name = "Валюты счетов", description = "Управление валютами банковских счетов")
public class AccountCurrencyController {
    private final AccountCurrencyService service;
    private final AccountCurrencyMapper mapper;
    @PostMapping("/save")
    @Operation(summary = "Добавить валюту", description = "Создание новой валюты для банковских счетов")
    public ResponseEntity<?> save(@RequestBody AccountCurrencyRequest request){
        service.save(mapper.toEntity(request));
        return ResponseEntity.ok("Валюта добавлена!");
    }
    @GetMapping("/getAll")
    @Operation(summary = "Получить список валют", description = "Получение списка всех валют с пагинацией")
    public ResponseEntity<?> getAll(
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(mapper.toResponseList(service.findAll(page,size)));
    }

    @GetMapping("/getByid/{id}")
    @Operation(summary = "Получить валюту по ID", description = "Получение информации о валюте по идентификатору")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить валюту", description = "Удаление валюты по идентификатору")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Валюта удалена!");
    }
}


