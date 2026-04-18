package com.example.nexobank2.dto.recordDto;

public record AccountInfo(
        Long id,
        String accountNumber,
        String ownerEmail
) {
}
