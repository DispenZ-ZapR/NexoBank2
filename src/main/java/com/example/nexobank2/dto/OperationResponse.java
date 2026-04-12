package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OperationResponse {
    private UUID id;
    private Long initiatorId;
    private String status;
    private String reason;
    private String channel;
    private LocalDateTime createdAt;
    private List<TransactionResponse> transactions;
}
