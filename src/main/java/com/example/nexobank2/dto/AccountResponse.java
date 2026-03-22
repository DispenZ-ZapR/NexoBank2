package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String accountType;
    private BigDecimal balance;
    private String accountNumber;
    private String currencyName;
    private String status;
}
