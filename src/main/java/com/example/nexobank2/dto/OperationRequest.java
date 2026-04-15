package com.example.nexobank2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OperationRequest {
    @NotBlank(message = "Причина операции обязательна!")
    @Size(max = 500, message = "Причина не должна превышать 500 символов")
    private String reason;
    @NotBlank(message = "Канал операции обязателен!")
    private String channel;
}
