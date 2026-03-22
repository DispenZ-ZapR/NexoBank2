package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class EmployeeRequest {
    private Long positionId;
    private BigDecimal salary;
    private Long userId;
}
