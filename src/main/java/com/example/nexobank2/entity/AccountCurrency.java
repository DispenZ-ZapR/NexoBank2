package com.example.nexobank2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account_currency")
public class AccountCurrency extends BaseEntity {

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @Size(max = 10)
    @NotNull
    @Column(name = "name", nullable = false, length = 10)
    private String name;

}