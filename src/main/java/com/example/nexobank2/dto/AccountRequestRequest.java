package com.example.nexobank2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountRequestRequest {
    
    @NotNull(message = "ID типа счета обязателен")
    private Long accountTypeId;
    
    @NotNull(message = "ID валюты обязателен")
    private Long currencyId;
}
