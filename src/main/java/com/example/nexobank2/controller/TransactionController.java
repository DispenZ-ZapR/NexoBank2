package com.example.nexobank2.controller;

import com.example.nexobank2.dto.TransactionResponse;
import com.example.nexobank2.entity.Transaction;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.mapper.TransactionMapper;
import com.example.nexobank2.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Транзакции", description = "API для просмотра истории транзакций")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    
    @GetMapping("/my")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Получить все мои транзакции",
        description = "Возвращает историю всех транзакций текущего пользователя"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список транзакций получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<TransactionResponse>> getMyTransactions(
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {
        
        List<Transaction> transactions = transactionService.getMyTransactions(currentUser.getId());
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }
    
    @GetMapping("/account/{accountId}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Получить транзакции конкретного счета",
        description = "Возвращает историю транзакций по указанному счету (только для владельца счета)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список транзакций получен"),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
        @ApiResponse(responseCode = "404", description = "Счет не найден"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser,
            @Parameter(description = "ID счета", required = true) @PathVariable @Min(1) Long accountId) {
        
        List<Transaction> transactions = transactionService.getAccountTransactions(accountId, currentUser.getId());
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }
}
