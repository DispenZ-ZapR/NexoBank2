package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private String userType;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private String firstName;
    private String lastName;
    private String middleName;
}
