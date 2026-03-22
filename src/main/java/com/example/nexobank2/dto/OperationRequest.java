package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OperationRequest {
    private String reason;
    private String channel;
}
