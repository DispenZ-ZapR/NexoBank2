package com.example.nexobank2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCurrencyResponse {
    private Long id;
    private String code;
    private String name;
    private String digitalCode;
}
