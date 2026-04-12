package com.example.nexobank2.service.impl;

import com.example.nexobank2.entity.*;
import com.example.nexobank2.enums.AccountRequestStatus;
import com.example.nexobank2.enums.AccountStatus;
import com.example.nexobank2.enums.ClientStatus;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.exception.RequestProcessed;
import com.example.nexobank2.exception.UnverifiedException;
import com.example.nexobank2.repository.AccountRequestRepository;
import com.example.nexobank2.service.AccountRequestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountRequestServiceImpl implements AccountRequestService {
    
    private final AccountRequestRepository accountRequestRepository;
    private final ClientServiceImpl clientService;
    private final EmployeeServiceImpl employeeService;
    private final AccountServiceImpl accountService;
    private final EmailServiceImpl emailService;
    
    @Override
    public List<AccountRequest> findAll() {
        return accountRequestRepository.findAll();
    }
    
    @Override
    @Transactional
    public AccountRequest save(AccountRequest entity) {
        return accountRequestRepository.save(entity);
    }
    
    @Override
    public AccountRequest findById(Long id) {
        return accountRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Счет не найден"));
    }
    
    @Override
    public void deleteById(Long id) {
        accountRequestRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public AccountRequest createRequest(Long clientId, Long accountTypeId, Long currencyId) {
        Client client = clientService.findById(clientId);
        
        if (client.getClientStatus() != ClientStatus.ACTIVE) {
            throw new UnverifiedException("Клиент не верифицирован");
        }
        
        AccountRequest request = new AccountRequest();
        request.setClient(client);
        
        // Получаем AccountType и Currency через accountService
        AccountType accountType = new AccountType();
        accountType.setId(accountTypeId);
        request.setAccountType(accountType);
        
        AccountCurrency currency = new AccountCurrency();
        currency.setId(currencyId);
        request.setCurrency(currency);
        
        request.setStatus(AccountRequestStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());
        
        AccountRequest savedRequest = accountRequestRepository.save(request);
        
        // Отправка уведомления клиенту
        emailService.sendSimpleMessage(
                client.getUser().getEmail(),
                "Заявка на открытие счета принята",
                "Ваша заявка на открытие счета принята и находится на рассмотрении. " +
                        "Вы получите уведомление после обработки заявки."
        );
        
        return savedRequest;
    }
    
    @Override
    @Transactional
    public AccountRequest approveRequest(Long requestId, Long employeeId) {
        AccountRequest request = findById(requestId);
        Employee employee = employeeService.findById(employeeId);
        
        if (request.getStatus() != AccountRequestStatus.PENDING) {
            throw new RequestProcessed("Заявка уже обработана");
        }
        
        // Создаем счет
        Account account = new Account();
        account.setClient(request.getClient());
        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);
        account.setDateCreated(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        
        accountService.save(account);
        
        request.setStatus(AccountRequestStatus.APPROVED);
        request.setApprovedBy(employee);
        request.setProcessedAt(LocalDateTime.now());
        
        AccountRequest updatedRequest = accountRequestRepository.save(request);
        
        // Отправка уведомления клиенту
        emailService.sendSimpleMessage(
                request.getClient().getUser().getEmail(),
                "Заявка на открытие счета одобрена",
                "Ваша заявка на открытие счета одобрена! " +
                        "Номер счета: " + account.getAccountNumber()
        );
        
        return updatedRequest;
    }
    
    @Override
    @Transactional
    public void rejectRequest(Long requestId, Long employeeId, String reason) {
        AccountRequest request = findById(requestId);
        Employee employee = employeeService.findById(employeeId);
        
        if (request.getStatus() != AccountRequestStatus.PENDING) {
            throw new RequestProcessed("Заявка уже обработана");
        }
        
        request.setStatus(AccountRequestStatus.REJECTED);
        request.setApprovedBy(employee);
        request.setRejectionReason(reason);
        request.setProcessedAt(LocalDateTime.now());
        
        accountRequestRepository.save(request);
        
        // Отправка уведомления клиенту
        emailService.sendSimpleMessage(
                request.getClient().getUser().getEmail(),
                "Заявка на открытие счета отклонена",
                "К сожалению, ваша заявка на открытие счета отклонена. " +
                        "Причина: " + reason
        );
    }
    
    @Override
    public List<AccountRequest> findByStatus(AccountRequestStatus status) {
        return accountRequestRepository.findByStatus(status);
    }
    
    @Override
    public List<AccountRequest> findByClientId(Long clientId) {
        return accountRequestRepository.findByClientId(clientId);
    }
    
    @Override
    public List<AccountRequest> findByClientIdAndStatus(Long clientId, AccountRequestStatus status) {
        return accountRequestRepository.findByClientIdAndStatus(clientId, status);
    }
    
    @Override
    public List<AccountRequest> getPendingRequests() {
        return accountRequestRepository.findByStatus(AccountRequestStatus.PENDING);
    }
    
    @Override
    public List<AccountRequest> findByApprovedById(Long employeeId) {
        return accountRequestRepository.findByApprovedById(employeeId);
    }
    
    // Генерация номера счета
    private String generateAccountNumber() {
        return "KG" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}
