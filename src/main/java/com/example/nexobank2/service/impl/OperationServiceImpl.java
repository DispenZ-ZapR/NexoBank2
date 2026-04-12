package com.example.nexobank2.service.impl;

import com.example.nexobank2.dto.TransactionRequest;
import com.example.nexobank2.entity.Account;
import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.Transaction;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.OperationStatus;
import com.example.nexobank2.enums.TransactionType;
import com.example.nexobank2.exception.BadRequestException;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.exception.ServerErrorException;
import com.example.nexobank2.repository.AccountRepository;
import com.example.nexobank2.repository.OperationRepository;
import com.example.nexobank2.repository.TransactionRepository;
import com.example.nexobank2.service.AccountService;
import com.example.nexobank2.service.OperationService;
import com.example.nexobank2.service.TransactionService;
import lombok.AllArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;
    @Named("getByUUID")
    @Override
    public Operation getByUUID(UUID uuid){
        return operationRepository.findById(uuid).orElseThrow(()-> new NotFoundException("Operation not found"));
    }

    @Override
    @Transactional
    public Operation transfer(TransactionRequest request, User initiator) {
        // 2. Проверяем что счета существуют
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new NotFoundException("Счёт отправителя не найден"));
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new NotFoundException("Счёт получателя не найден"));

        // 3. Проверяем что счёт принадлежит клиенту
        if (!fromAccount.getClient().getUser().getId().equals(initiator.getId())) {
            throw new BadRequestException("Счёт не принадлежит пользователю");
        }

        // 5. Создаём Operation со статусом PENDING
        Operation operation = new Operation();
        operation.setInitiator(initiator);
        operation.setChannel("WEB");
        operation.setStatus(OperationStatus.PENDING);
        operation.setReason(request.getReason());
        operation.setCreatedAt(LocalDateTime.now());
        operation = operationRepository.save(operation);

        try {
            // 4, 6, 7. Проверка баланса + списание + зачисление
            accountService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
            
            // Обновляем объекты после изменения балансов
            fromAccount = accountRepository.findById(request.getFromAccountId()).get();
            toAccount = accountRepository.findById(request.getToAccountId()).get();
            
            // Создаём Transaction записи
            transactionService.record(fromAccount, toAccount, request.getAmount(), operation);

            // 8. Меняем статус Operation на COMPLETED
            operation.setStatus(OperationStatus.SUCCESSFULLY);
            operationRepository.save(operation);

            return operation;
        } catch (Exception e) {
            // 9. Если что-то упало → статус FAILED, откат транзакции
            operation.setStatus(OperationStatus.FAILED);
            operationRepository.save(operation);
            throw new ServerErrorException("Ошибка при выполнении перевода: " + e.getMessage());
        }
    }

    @Override
    public List<Operation> getByAll() {
        return operationRepository.findAll();
    }

    public Operation save(Operation operation) {
        return operationRepository.save(operation);
    }

    @Override
    public List<Operation> findByInitiatorId(Long id) {
        return operationRepository.findByInitiatorId(id).orElseThrow(() -> new NotFoundException("operation not found"));
    }

    @Override
    public List<Operation> findByStatus(OperationStatus status) {
        return operationRepository.findByStatus(status);
    }
}
