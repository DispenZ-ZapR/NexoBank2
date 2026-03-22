package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransactionRequest {
    private Long accountId;
    private String transactionType;
    private BigDecimal amount;
    private UUID operationId;
}
