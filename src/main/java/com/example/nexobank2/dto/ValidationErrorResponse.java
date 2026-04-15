package com.example.nexobank2.dto;

import java.util.Map;

public record ValidationErrorResponse(String message, int status, Map<String, String> errors) {
}
