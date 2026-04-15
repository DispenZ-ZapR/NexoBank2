package com.example.nexobank2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    @Min(1)
    @NotNull(message = "Это поле обязательно!")
    private Long accountTypeId;
    @Size(min = 10)
    @NotNull(message = "Это поле обязательно!")
    private String accountNumber;
    @Min(1)
    @NotNull(message = "Это поле обязательно!")
    private Long currencyId;
}
