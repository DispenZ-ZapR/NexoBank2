package com.example.nexobank2.dto;

import com.example.nexobank2.enums.ClientStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClientResponse {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private Integer creditRating;
    private Long accountId;
    private ClientStatus clientStatus;
}

