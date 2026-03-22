package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OperationResponse {
    private UUID id;
    private String status;
    private String reason;
    private String channel;
    private LocalDateTime createdAt;
    private Long initiatorId;
}
