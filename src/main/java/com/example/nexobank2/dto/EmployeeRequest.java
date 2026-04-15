package com.example.nexobank2.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @Email(message = "Неверный формат электронной почты")
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 10, max = 15, message = "Номмер должен быть от 9 до 15 цифр")
    @Pattern(regexp = "^\\+0?[0-9]{11,15}$", message = "Номер телефона должен содержать 11-15 цифр")
    private String phoneNumber;

    // данные для passport
    @NotBlank
    @Size(min = 2, max = 50,message = "Имя должно быть от 2 до 50 символов")
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 50,message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;
    private String middleName;
    @Past
    @NotNull(message = "Дата рождения обязательна!")
    private LocalDate dateOfBirth;
    private String personalNumber;
    private String passportNumber;

    // данные специфичные для employee
    @NotNull(message = "Должность обязательна!")
    @Min(value = 1, message = "ID должности должен быть положительным")
    private Long positionId;
    @NotNull(message = "Зарплата обязательна!")
    @DecimalMin(value = "0.01", message = "Зарплата должна быть больше 0")
    @Digits(integer = 19, fraction = 4, message = "Некорректный формат зарплаты")
    private BigDecimal salary;
}
