package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PassportResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String PersonalNumber;
    private boolean isLost;
    private String PassportNumber;
}
