package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.AccountRequestRequest;
import com.example.nexobank2.dto.AccountRequestResponse;
import com.example.nexobank2.entity.AccountRequest;
import com.example.nexobank2.enums.AccountRequestStatus;
import com.example.nexobank2.mapper.AccountRequestMapper;
import com.example.nexobank2.service.impl.AccountRequestServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-requests")
@AllArgsConstructor
@Validated
@Tag(name = "Account Request", description = "Управление заявками на открытие счетов")
public class AccountRequestController {
    
    private final AccountRequestServiceImpl accountRequestService;
    private final AccountRequestMapper accountRequestMapper;
    
    @PostMapping
    @Operation(summary = "Создать заявку на открытие счета")
    public ResponseEntity<AccountRequestResponse> createRequest(@Valid @RequestBody AccountRequestRequest request) {
        AccountRequest accountRequest = accountRequestService.createRequest(
                request.getClientId(),
                request.getAccountTypeId(),
                request.getCurrencyId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountRequestMapper.toResponse(accountRequest));
    }
    
    @GetMapping
    @Operation(summary = "Получить все заявки")
    public ResponseEntity<List<AccountRequestResponse>> getAllRequests() {
        List<AccountRequest> requests = accountRequestService.findAll();
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    public ResponseEntity<AccountRequestResponse> getRequestById(@PathVariable @Min(1) Long id) {
        AccountRequest request = accountRequestService.findById(id);
        return ResponseEntity.ok(accountRequestMapper.toResponse(request));
    }
    
    @GetMapping("/pending")
    @Operation(summary = "Получить все ожидающие заявки")
    public ResponseEntity<List<AccountRequestResponse>> getPendingRequests() {
        List<AccountRequest> requests = accountRequestService.getPendingRequests();
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Получить заявки клиента")
    public ResponseEntity<List<AccountRequestResponse>> getRequestsByClient(@PathVariable @Min(1) Long clientId) {
        List<AccountRequest> requests = accountRequestService.findByClientId(clientId);
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @GetMapping("/status/")
    @Operation(summary = "Получить заявки по статусу")
    public ResponseEntity<List<AccountRequestResponse>> getRequestsByStatus(@RequestBody @Valid AccountRequestStatus status) {
        List<AccountRequest> requests = accountRequestService.findByStatus(status);
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
    
    @PostMapping("/{id}/approve")
    @Operation(summary = "Одобрить заявку")
    public ResponseEntity<AccountRequestResponse> approveRequest(
            @PathVariable @Min(1) Long id,
            @RequestParam @Min(1) Long employeeId) {
        AccountRequest request = accountRequestService.approveRequest(id, employeeId);
        return ResponseEntity.ok(accountRequestMapper.toResponse(request));
    }
    
    @PostMapping("/{id}/reject")
    @Operation(summary = "Отклонить заявку")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable @Min(1) Long id,
            @RequestParam @Min(1) Long employeeId,
            @RequestParam @NotBlank String reason) {
        accountRequestService.rejectRequest(id, employeeId, reason);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Получить заявки, обработанные сотрудником")
    public ResponseEntity<List<AccountRequestResponse>> getRequestsByEmployee(@PathVariable @Min(1) Long employeeId) {
        List<AccountRequest> requests = accountRequestService.findByApprovedById(employeeId);
        return ResponseEntity.ok(accountRequestMapper.toResponseList(requests));
    }
}
