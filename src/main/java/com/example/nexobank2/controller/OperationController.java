package com.example.nexobank2.controller;

import com.example.nexobank2.dto.OperationResponse;
import com.example.nexobank2.dto.TransactionRequest;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.OperationStatus;
import com.example.nexobank2.mapper.OperationMapper;
import com.example.nexobank2.service.OperationService;
import com.example.nexobank2.service.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "Операции", description = "API для управления банковскими операциями и переводами")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class OperationController {

    private final OperationService operationService;
    private final TransactionService transactionService;
    private final OperationMapper operationMapper;

    @GetMapping
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Получить все операции",
        description = "Возвращает постраничный список всех банковских операций с транзакциями"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список операций получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<OperationResponse>> getAllOperations(
        @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        List<Operation> operations = operationService.getAll(page,size);
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/my")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Получить мои операции",
        description = "Возвращает все операции текущего авторизованного пользователя"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список операций получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<OperationResponse>> getMyOperations(
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {
        
        List<Operation> operations = operationService.getMyOperations(currentUser.getId());
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{uuid}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Получить операцию по UUID",
        description = "Возвращает детальную информацию об операции по её уникальному идентификатору"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Операция найдена"),
        @ApiResponse(responseCode = "404", description = "Операция не найдена")
    })
    public ResponseEntity<OperationResponse> getOperationByUuid(
        @Parameter(description = "UUID операции", required = true) @PathVariable @NotNull UUID uuid) {
        Operation operation = operationService.getByUUID(uuid);
        return ResponseEntity.ok(operationMapper.toResponse(operation, transactionService.findByOperationId(uuid)));
    }

    @PostMapping("/transfer")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Перевод между счетами",
        description = "Выполняет денежный перевод между двумя счетами. Операция выполняется в транзакции с проверкой баланса"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен"),
        @ApiResponse(responseCode = "400", description = "Недостаточно средств или некорректные данные"),
        @ApiResponse(responseCode = "404", description = "Счет не найден"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<OperationResponse> transfer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Данные перевода (счет отправителя, счет получателя, сумма)",
                required = true
            ) @RequestBody @Valid TransactionRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {
        
        Operation operation = operationService.transfer(request, currentUser);
        return ResponseEntity.ok(operationMapper.toResponse(operation, transactionService.findByOperationId(operation.getId())));
    }

    @GetMapping("/getByStatus")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Получить операции по статусу",
        description = "Возвращает список операций с указанным статусом (SUCCESS, FAILED, PENDING)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список операций получен")
    })
    public ResponseEntity<List<OperationResponse>> getByStatus(
        @Parameter(description = "Статус операции", required = true) @RequestParam @NotNull OperationStatus status) {
        List<Operation> operations = operationService.findByStatus(status);
        List<OperationResponse> responses = operations.stream()
                .map(op -> operationMapper.toResponse(op, transactionService.findByOperationId(op.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }
}
