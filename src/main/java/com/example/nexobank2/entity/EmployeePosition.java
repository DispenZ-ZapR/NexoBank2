package com.example.nexobank2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "employee_position")
public class EmployeePosition extends BaseEntity {
    @Size(max = 20)
    @NotNull
    @Column(name = "name", nullable = false, length = 20)
    private String name;

@ManyToMany(mappedBy = "positions")
    private Set<Employee> employees;

}