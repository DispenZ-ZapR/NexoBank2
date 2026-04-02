package com.example.nexobank2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String email;
    private String phoneNumber;

    // данные для passport
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String personalNumber;
    private String passportNumber;

    // данные специфичные для employee
    private Long positionId;
    private BigDecimal salary;
}
