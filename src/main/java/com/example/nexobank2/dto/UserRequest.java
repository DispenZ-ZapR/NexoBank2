package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private Long passportId;
    private String email;
    private String password;
    private String phoneNumber;
    private String userType;
}
