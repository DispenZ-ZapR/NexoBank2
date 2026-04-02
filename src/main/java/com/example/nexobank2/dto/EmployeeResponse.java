package com.example.nexobank2.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({
    "id",
    "firstName",
    "lastName",
    "middleName",
    "email",
    "phoneNumber",
    "positions",
    "salary",
    "employeeStatus",
    "hiredAt",
    "createdAt",
    "deletedAt"
})
public class EmployeeResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    // данные паспорта
    private String firstName;
    private String lastName;
    private String middleName;
    // данные сотрудника
    private List<String> positions;        // список должностей
    private BigDecimal salary;
    private LocalDateTime hiredAt;
    private String employeeStatus;

}
