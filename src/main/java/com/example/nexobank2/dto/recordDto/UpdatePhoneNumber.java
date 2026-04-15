package com.example.nexobank2.dto.recordDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePhoneNumber(
        @NotBlank(message = "Номер телефона обязателен!")
        @Size(min = 10, max = 15, message = "Номер телефона должен быть от 11 до 15 символов")
        String phoneNumber) {
}
