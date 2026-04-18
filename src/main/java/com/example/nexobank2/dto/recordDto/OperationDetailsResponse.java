package com.example.nexobank2.dto.recordDto;

import com.example.nexobank2.dto.TransactionResponse;
import com.example.nexobank2.enums.OperationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OperationDetailsResponse(
        UUID id,
        LocalDateTime createdAt,
        OperationStatus status,
        String reason,
        String channel,
        List<TransactionResponse> transactions
) {
}
