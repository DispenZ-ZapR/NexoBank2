package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransactionRequest {
    private Long fromAccountId;  // счёт откуда
    private Long toAccountId;    // счёт куда
    private BigDecimal amount;
    private String reason;
}
