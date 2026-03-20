package com.example.nexobank2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "passport")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 15)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 15)
    private String firstName;

    @Size(max = 30)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Size(max = 30)
    @Column(name = "middle_name", length = 30)
    private String middleName;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Size(max = 255)
    @NotNull
    @Column(name = "personal_number", nullable = false)
    private String personalNumber;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_lost", nullable = false)
    private Boolean isLost = false;

    @Size(max = 255)
    @NotNull
    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

}