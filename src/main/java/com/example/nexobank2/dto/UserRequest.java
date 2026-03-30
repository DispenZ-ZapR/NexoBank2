package com.example.nexobank2.dto;

import com.example.nexobank2.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequest {
    private String email;
    private String phoneNumber;
    // данные паспорта
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String personalNumber;
    private String passportNumber;
}
