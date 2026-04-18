package com.example.nexobank2.dto.recordDto;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email String email, String password) {
}
