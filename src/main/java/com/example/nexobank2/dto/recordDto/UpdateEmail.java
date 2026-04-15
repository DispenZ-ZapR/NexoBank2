package com.example.nexobank2.dto.recordDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmail(
        @NotBlank(message = "email обязателен!")
        @Email(message = "Некорректный email")
        String email) {
}
