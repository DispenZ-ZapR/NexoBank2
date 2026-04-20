package com.example.nexobank2.controller;

import com.example.nexobank2.dto.OperationResponse;
import com.example.nexobank2.dto.TransactionRequest;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.OperationStatus;
import com.example.nexobank2.mapper.OperationMapper;
import com.example.nexobank2.service.OperationService;
import com.example.nexobank2.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/operations")
@AllArgsConstructor
@Tag(name = "Operation", description = "Управление операциями")
@Validated
public class OperationController {

    private final OperationService operationService;
    private final TransactionService transactionService;
    private final OperationMapper operationMapper;

    @GetMapping
    @io.swagger.v3.oas.annotations.Operation(summary = "Получить все операции")
    public ResponseEntity<List<OperationResponse>> getAllOperations(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<Operation> operations = operationService.getAll(page,size);
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/my")
    @io.swagger.v3.oas.annotations.Operation(summary = "Получить мои операции")
    public ResponseEntity<List<OperationResponse>> getMyOperations(
            @AuthenticationPrincipal User currentUser) {
        
        List<Operation> operations = operationService.getMyOperations(currentUser.getId());
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{uuid}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Получить операцию по UUID")
    public ResponseEntity<OperationResponse> getOperationByUuid(@PathVariable @NotNull UUID uuid) {
        Operation operation = operationService.getByUUID(uuid);
        return ResponseEntity.ok(operationMapper.toResponse(operation, transactionService.findByOperationId(uuid)));
    }

    @PostMapping("/transfer")
    @io.swagger.v3.oas.annotations.Operation(summary = "Перевод между счетами")
    public ResponseEntity<OperationResponse> transfer(
            @RequestBody @Valid TransactionRequest request,
            @AuthenticationPrincipal User currentUser) {
        
        Operation operation = operationService.transfer(request, currentUser);
        return ResponseEntity.ok(operationMapper.toResponse(operation, transactionService.findByOperationId(operation.getId())));
    }

    @GetMapping("/getByStatus")
    public ResponseEntity<List<OperationResponse>> getByStatus(@RequestParam @NotNull OperationStatus status) {
        List<Operation> operations = operationService.findByStatus(status);
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }
}
