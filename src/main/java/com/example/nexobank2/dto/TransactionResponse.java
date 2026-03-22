package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
public class TransactionResponse {
    private Long id;
    private Long accountId;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal transactionAfter;
    private LocalDateTime transactionDate;
    private UUID operationId;
}
