package com.example.nexobank2.controller;

import com.example.nexobank2.dto.AccountResponse;
import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.Client;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.AccountStatus;
import com.example.nexobank2.mapper.AccountMapper;
import com.example.nexobank2.service.AccountService;
import com.example.nexobank2.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
@Tag(name = "Счета", description = "API для управления банковскими счетами")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class AccountController {
    
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final ClientService clientService;
    
    @GetMapping
    @Operation(
        summary = "Получить список всех счетов",
        description = "Возвращает постраничный список всех банковских счетов в системе"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список счетов успешно получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<AccountResponse>> getAllAccounts(
        @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        List<Account> accounts = accountService.findAll(page,size);
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Получить счет по ID",
        description = "Возвращает детальную информацию о счете по его идентификатору"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Счет найден"),
        @ApiResponse(responseCode = "404", description = "Счет не найден"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<AccountResponse> getAccountById(
        @Parameter(description = "ID счета", required = true) @PathVariable @Min(1) Long id) {
        Account account = accountService.findById(id);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
    
    @GetMapping("/number/{accountNumber}")
    @Operation(
        summary = "Получить счет по номеру",
        description = "Возвращает информацию о счете по его уникальному номеру"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Счет найден"),
        @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    public ResponseEntity<AccountResponse> getAccountByNumber(
        @Parameter(description = "Номер счета", required = true) @PathVariable String accountNumber) {
        Account account = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
    
    @GetMapping("/client/{clientId}")
    @Operation(
        summary = "Получить все счета клиента",
        description = "Возвращает список всех счетов конкретного клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список счетов получен"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<List<AccountResponse>> getAccountsByClient(
        @Parameter(description = "ID клиента", required = true) @PathVariable @Min(1) Long clientId) {
        List<Account> accounts = accountService.findByClientId(clientId);
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/client/{clientId}/active")
    @Operation(
        summary = "Получить активные счета клиента",
        description = "Возвращает только активные счета конкретного клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список активных счетов получен"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<List<AccountResponse>> getActiveAccountsByClient(
        @Parameter(description = "ID клиента", required = true) @PathVariable @Min(1) Long clientId) {
        List<Account> accounts = accountService.findActiveAccountsByClientId(clientId);
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/status")
    @Operation(
        summary = "Получить счета по статусу",
        description = "Возвращает список счетов с указанным статусом (ACTIVE, BLOCKED, FROZEN, CLOSED)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список счетов получен")
    })
    public ResponseEntity<List<AccountResponse>> getAccountsByStatus(
        @Parameter(description = "Статус счета", required = true) @RequestParam @NotNull AccountStatus status) {
        List<Account> accounts = accountService.findByStatus(status);
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
    
    @GetMapping("/{id}/balance")
    @Operation(
        summary = "Получить баланс счета",
        description = "Возвращает текущий баланс счета"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Баланс получен"),
        @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    public ResponseEntity<BigDecimal> getBalance(
        @Parameter(description = "ID счета", required = true) @PathVariable @Min(1) Long id) {
        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(balance);
    }
    
    @PutMapping("/{id}/status")
    @Operation(
        summary = "Изменить статус счета",
        description = "Изменяет статус счета на указанный"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Статус успешно изменен"),
        @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    public ResponseEntity<Void> changeStatus(
            @Parameter(description = "ID счета", required = true) @PathVariable @Min(1) Long id,
            @Parameter(description = "Новый статус", required = true) @RequestParam @NotNull AccountStatus status) {
        accountService.changeAccountStatus(id, status);
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/{id}/unblock")
    @Operation(
        summary = "Разблокировать счет",
        description = "Разблокирует заблокированный счет, переводя его в активное состояние"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Счет успешно разблокирован"),
        @ApiResponse(responseCode = "404", description = "Счет не найден"),
        @ApiResponse(responseCode = "400", description = "Счет не заблокирован")
    })
    public ResponseEntity<Void> unblockAccount(
        @Parameter(description = "ID счета", required = true) @PathVariable @Min(1) Long id) {
        accountService.unblockAccount(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/freeze")
    @Operation(
        summary = "Заморозить счет",
        description = "Замораживает счет, блокируя все операции по нему"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Счет успешно заморожен"),
        @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    public ResponseEntity<Void> freezeAccount(
        @Parameter(description = "ID счета", required = true) @PathVariable @Min(1) Long id) {
        accountService.freezeAccount(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить счет",
        description = "Блокирует счет (мягкое удаление)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Счет успешно удален"),
        @ApiResponse(responseCode = "404", description = "Счет не найден")
    })
    public ResponseEntity<Void> deleteAccount(
        @Parameter(description = "ID счета", required = true) @PathVariable @Min(1) Long id) {
        accountService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/myAccounts")
    @Operation(
        summary = "Получить мои счета",
        description = "Возвращает список всех счетов текущего авторизованного клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список счетов получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<AccountResponse>> getAccounts(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        Client client = clientService.findByUserId(user.getId());
        List<Account> accounts = accountService.findByClientId(client.getId());
        return ResponseEntity.ok(accountMapper.toResponseList(accounts));
    }
}
