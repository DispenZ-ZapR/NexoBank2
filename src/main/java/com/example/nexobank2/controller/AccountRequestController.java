package com.example.nexobank2.controller;

import com.example.nexobank2.dto.AccountRequestRequest;
import com.example.nexobank2.dto.AccountRequestResponse;
import com.example.nexobank2.entity.AccountRequest;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.AccountRequestStatus;
import com.example.nexobank2.mapper.AccountRequestMapper;
import com.example.nexobank2.service.AccountRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-requests")
@AllArgsConstructor
@Validated
@Tag(name = "Заявки на счета", description = "API для управления заявками на открытие банковских счетов")
@SecurityRequirement(name = "bearerAuth")
public class AccountRequestController {
    
    private final AccountRequestService accountRequestService;
    private final AccountRequestMapper accountRequestMapper;
    
    @PostMapping("/createRequest")
    @Operation(
        summary = "Создать заявку на открытие счета",
        description = "Создает новую заявку на открытие банковского счета для клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Заявка успешно создана"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<AccountRequestResponse> createRequest(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные заявки", required = true) @Valid @RequestBody AccountRequestRequest request, @AuthenticationPrincipal User user) {
        AccountRequest accountRequest = accountRequestService.createRequest(
                user.getClient().getId(),
                request.getAccountTypeId(),
                request.getCurrencyId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountRequestMapper.toResponse(accountRequest));
    }
    
    @GetMapping
    @Operation(
        summary = "Получить все заявки",
        description = "Возвращает постраничный список всех заявок на открытие счетов"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список заявок получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<AccountRequestResponse>> getAllRequests(
        @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        List<AccountRequest> requests = accountRequestService.findAll(page,size);
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Получить заявку по ID",
        description = "Возвращает детальную информацию о заявке по её идентификатору"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Заявка найдена"),
        @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    public ResponseEntity<AccountRequestResponse> getRequestById(
        @Parameter(description = "ID заявки", required = true) @PathVariable @Min(1) Long id) {
        AccountRequest request = accountRequestService.findById(id);
        return ResponseEntity.ok(accountRequestMapper.toResponse(request));
    }
    
    @GetMapping("/pending")
    @Operation(
        summary = "Получить все ожидающие заявки",
        description = "Возвращает список заявок со статусом PENDING (ожидают рассмотрения)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список ожидающих заявок получен")
    })
    public ResponseEntity<List<AccountRequestResponse>> getPendingRequests() {
        List<AccountRequest> requests = accountRequestService.getPendingRequests();
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @GetMapping("/client/{clientId}")
    @Operation(
        summary = "Получить заявки клиента",
        description = "Возвращает все заявки конкретного клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список заявок получен"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<List<AccountRequestResponse>> getRequestsByClient(
        @Parameter(description = "ID клиента", required = true) @PathVariable @Min(1) Long clientId) {
        List<AccountRequest> requests = accountRequestService.findByClientId(clientId);
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @GetMapping("/status/")
    @Operation(
        summary = "Получить заявки по статусу",
        description = "Возвращает список заявок с указанным статусом (PENDING, APPROVED, REJECTED)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список заявок получен")
    })
    public ResponseEntity<List<AccountRequestResponse>> getRequestsByStatus(
        @Parameter(description = "Статус заявки", required = true) @RequestParam @NotNull AccountRequestStatus status) {
        List<AccountRequest> requests = accountRequestService.findByStatus(status);
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @PostMapping("/{id}/approve")
    @Operation(
        summary = "Одобрить заявку",
        description = "Одобряет заявку на открытие счета и автоматически создает счет для клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Заявка одобрена, счет создан"),
        @ApiResponse(responseCode = "404", description = "Заявка или сотрудник не найдены"),
        @ApiResponse(responseCode = "400", description = "Заявка уже обработана")
    })
    public ResponseEntity<AccountRequestResponse> approveRequest(
            @Parameter(description = "ID заявки", required = true) @PathVariable @Min(1) Long id,
            @Parameter(description = "ID сотрудника, одобряющего заявку", required = true) @RequestParam @Min(1) Long employeeId) {
        AccountRequest request = accountRequestService.approveRequest(id, employeeId);
        return ResponseEntity.ok(accountRequestMapper.toResponse(request));
    }
    
    @PostMapping("/{id}/reject")
    @Operation(
        summary = "Отклонить заявку",
        description = "Отклоняет заявку на открытие счета с указанием причины отказа"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Заявка отклонена"),
        @ApiResponse(responseCode = "404", description = "Заявка или сотрудник не найдены"),
        @ApiResponse(responseCode = "400", description = "Заявка уже обработана")
    })
    public ResponseEntity<Void> rejectRequest(
            @Parameter(description = "ID заявки", required = true) @PathVariable @Min(1) Long id,
            @Parameter(description = "ID сотрудника, отклоняющего заявку", required = true) @RequestParam @Min(1) Long employeeId,
            @Parameter(description = "Причина отклонения", required = true) @RequestParam @NotBlank String reason) {
        accountRequestService.rejectRequest(id, employeeId, reason);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/employee/{employeeId}")
    @Operation(
        summary = "Получить заявки, обработанные сотрудником",
        description = "Возвращает список заявок, которые были одобрены или отклонены конкретным сотрудником"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список заявок получен"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<List<AccountRequestResponse>> getRequestsByEmployee(
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long employeeId) {
        List<AccountRequest> requests = accountRequestService.findByApprovedById(employeeId);
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }

    @GetMapping("/myClient")
    @Operation(
        summary = "Получить мои заявки (клиент)",
        description = "Возвращает все заявки текущего авторизованного клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список заявок получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<AccountRequestResponse>> getMyRequest(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountRequestMapper.toResponseList(accountRequestService.getMyRequest(user.getId())));
    }

    @GetMapping("/meEmployee")
    @Operation(
        summary = "Получить мои обработанные заявки (сотрудник)",
        description = "Возвращает все заявки, обработанные текущим авторизованным сотрудником"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список заявок получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<List<AccountRequestResponse>> getMyCheckedRequest(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountRequestMapper.toResponseList(accountRequestService.getMyCheckedRequest(user.getId())));
    }
}
