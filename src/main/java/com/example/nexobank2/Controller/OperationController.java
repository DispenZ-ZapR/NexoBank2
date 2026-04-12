package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.OperationResponse;
import com.example.nexobank2.dto.TransactionRequest;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.OperationStatus;
import com.example.nexobank2.mapper.OperationMapper;
import com.example.nexobank2.service.OperationService;
import com.example.nexobank2.service.TransactionService;
import com.example.nexobank2.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/operations")
@AllArgsConstructor
@Tag(name = "Operation", description = "Управление операциями")
public class OperationController {

    private final OperationService operationService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final OperationMapper operationMapper;

    @GetMapping
    @io.swagger.v3.oas.annotations.Operation(summary = "Получить все операции")
    public ResponseEntity<List<OperationResponse>> getAllOperations() {
        List<Operation> operations = operationService.getByAll();
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{uuid}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Получить операцию по UUID")
    public ResponseEntity<OperationResponse> getOperationByUuid(@PathVariable UUID uuid) {
        Operation operation = operationService.getByUUID(uuid);
        return ResponseEntity.ok(operationMapper.toResponse(operation, transactionService.findByOperationId(uuid)));
    }

    @PostMapping("/transfer")
    @io.swagger.v3.oas.annotations.Operation(summary = "Перевод между счетами")
    public ResponseEntity<OperationResponse> transfer(
            @RequestBody TransactionRequest request,
            @RequestParam Long userId) {
        User initiator = userService.findById(userId);
        Operation operation = operationService.transfer(request, initiator);
        return ResponseEntity.ok(operationMapper.toResponse(operation, transactionService.findByOperationId(operation.getId())));
    }

    @GetMapping("/getByInitiatorId/{id}")
    public ResponseEntity<List<OperationResponse>> getByInitiatorId(@PathVariable Long id) {
        List<Operation> operations = operationService.findByInitiatorId(id);
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/getByStatus")
    public ResponseEntity<List<OperationResponse>> getByStatus(OperationStatus status) {
        List<Operation> operations = operationService.findByStatus(status);
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }
}
