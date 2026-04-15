package com.example.nexobank2.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyRequest {
    @Min(3)
    private int code;
    private String name;
}
