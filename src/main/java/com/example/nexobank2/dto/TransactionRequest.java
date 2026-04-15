package com.example.nexobank2.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransactionRequest {
    @NotNull(message = "Счет отправителя обязателен!")
    @Min(value = 1, message = "ID счета должен быть положительным")
    private Long fromAccountId;

    @NotNull(message = "Счет получателя обязателен!")
    @Min(value = 1, message = "ID счета должен быть положительным")
    private Long toAccountId;

    @NotNull(message = "Сумма обязательна!")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
    @Digits(integer = 10, fraction = 2, message = "Некорректный формат суммы")
    private BigDecimal amount;

    @Size(max = 255, message = "Причина не должна превышать 255 символов")
    private String reason;
}
