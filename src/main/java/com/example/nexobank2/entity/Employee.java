package com.example.nexobank2.entity;

import com.example.nexobank2.enums.EmployeeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity{

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private EmployeePosition position;

    @NotNull
    @Column(name = "salary", nullable = false, precision = 19, scale = 4)
    private BigDecimal salary;

    @NotNull
    @Column(name = "hired_at", nullable = false)
    private LocalDateTime hiredAt;

    @Size(max = 15)
    @NotNull
    @ColumnDefault("'ACTIVE'")
    @Column(name = "employee_status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}