package com.example.nexobank2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private EmployeePosition position;

    @NotNull
    @Column(name = "salary", nullable = false, precision = 19, scale = 4)
    private BigDecimal salary;

    @NotNull
    @Column(name = "hired_at", nullable = false)
    private OffsetDateTime hiredAt;

    @Size(max = 15)
    @NotNull
    @ColumnDefault("'ACTIVE'")
    @Column(name = "employee_status", nullable = false, length = 15)
    private String employeeStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}