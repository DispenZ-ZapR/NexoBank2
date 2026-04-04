package com.example.nexobank2.dto;

import com.example.nexobank2.enums.ClientStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientRequest {
    private String email;
    private String phoneNumber;

    private String firstName;
    private String lastName;
    private String middleName;      // необязательно
    private LocalDate dateOfBirth;
    private String personalNumber;
    private String passportNumber;
}
