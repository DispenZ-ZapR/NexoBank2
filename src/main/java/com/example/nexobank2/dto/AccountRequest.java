package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private Long accountTypeId;
    private String accountNumber;
    private Long currencyId;
}
