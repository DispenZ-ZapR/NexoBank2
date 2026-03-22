package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String userType;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private boolean isActive;
    private LocalDateTime deletedAt;
}
