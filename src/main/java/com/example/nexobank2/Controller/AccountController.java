package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.AccountResponse;
import com.example.nexobank2.entity.Account;
import com.example.nexobank2.enums.AccountStatus;
import com.example.nexobank2.mapper.AccountMapper;
import com.example.nexobank2.service.impl.AccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
@Tag(name = "Account", description = "Управление счетами")
@Validated
public class AccountController {
    
    private final AccountServiceImpl accountService;
    private final AccountMapper accountMapper;
    
    @GetMapping
    @Operation(summary = "Получить все счета")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить счет по ID")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable @Min(1) Long id) {
        Account account = accountService.findById(id);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
    
    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Получить счет по номеру")
    public ResponseEntity<AccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        Account account = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
    
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Получить счета клиента")
    public ResponseEntity<List<AccountResponse>> getAccountsByClient(@PathVariable @Min(1) Long clientId) {
        List<Account> accounts = accountService.findByClientId(clientId);
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/client/{clientId}/active")
    @Operation(summary = "Получить активные счета клиента")
    public ResponseEntity<List<AccountResponse>> getActiveAccountsByClient(@PathVariable @Min(1) Long clientId) {
        List<Account> accounts = accountService.findActiveAccountsByClientId(clientId);
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/status")
    @Operation(summary = "Получить счета по статусу")
    public ResponseEntity<List<AccountResponse>> getAccountsByStatus(@RequestParam @NotNull AccountStatus status) {
        List<Account> accounts = accountService.findByStatus(status);
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/{id}/balance")
    @Operation(summary = "Получить баланс счета")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable @Min(1) Long id) {
        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(balance);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Изменить статус счета")
    public ResponseEntity<Void> changeStatus(
            @PathVariable @Min(1) Long id,
            @RequestParam @NotBlank AccountStatus status) {
        accountService.changeAccountStatus(id, status);
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/{id}/unblock")
    @Operation(summary = "Разблокировать счет")
    public ResponseEntity<Void> unblockAccount(@PathVariable @Min(1) Long id) {
        accountService.unblockAccount(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/freeze")
    @Operation(summary = "Заморозить счет")
    public ResponseEntity<Void> freezeAccount(@PathVariable @Min(1) Long id) {
        accountService.freezeAccount(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить счет (заблокировать)")
    public ResponseEntity<Void> deleteAccount(@PathVariable @Min(1) Long id) {
        accountService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
