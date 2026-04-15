package com.example.nexobank2.dto.recordDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivationAccount(
        @NotBlank(message = "Введите пароль!")
        @Size(min = 6, message = "Пароль должен состоять минимум из 6 символов ")
        String password) {
}
