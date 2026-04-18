package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.TransactionResponse;
import com.example.nexobank2.entity.Transaction;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.mapper.TransactionMapper;
import com.example.nexobank2.service.TransactionService;
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
@Tag(name = "Transaction", description = "Управление транзакциями")
@Validated
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    
    @GetMapping("/my")
    @io.swagger.v3.oas.annotations.Operation(summary = "Получить все мои транзакции")
    public ResponseEntity<List<TransactionResponse>> getMyTransactions(
            @AuthenticationPrincipal User currentUser) {
        
        List<Transaction> transactions = transactionService.getMyTransactions(currentUser.getId());
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }
    
    @GetMapping("/account/{accountId}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Получить транзакции конкретного счета")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(
            @AuthenticationPrincipal User currentUser,
            @PathVariable @Min(1) Long accountId) {
        
        List<Transaction> transactions = transactionService.getAccountTransactions(accountId, currentUser.getId());
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }
}
