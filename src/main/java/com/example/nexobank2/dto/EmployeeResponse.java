package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeResponse {
    private Long id;
    private String position;
    private BigDecimal salary;
    private LocalDateTime hiredAt;
    private String employeeStatus;
    private Long userId;
}
