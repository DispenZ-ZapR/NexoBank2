package com.example.nexobank2.dto;

import com.example.nexobank2.enums.ClientStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "firstName" ,
        "lastName" ,
        "middleName" ,
        "email" ,
        "phoneNumber" ,
        "creditRating" ,
        "dateOfBirth" ,
        "createdAt" ,
        "deletedAt"
})
public class ClientResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Integer creditRating;
    // паспортные данные разворачиваем сюда
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
}

