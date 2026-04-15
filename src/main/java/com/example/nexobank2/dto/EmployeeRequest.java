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
    @NotBlank(message = "Email обязателен!")
    @Email(message = "Неверный формат электронной почты")
    private String email;
    
    @NotBlank(message = "Номер телефона обязателен!")
    @Pattern(regexp = "^(\\+996|996)[0-9]{9}$", message = "Формат: +996700123456 или 996700123456")
    private String phoneNumber;

    // данные для passport
    @NotBlank(message = "Имя обязательно!")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;
    
    @NotBlank(message = "Фамилия обязательна!")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;
    
    private String middleName;
    
    @NotNull(message = "Дата рождения обязательна!")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "ИНН обязателен!")
    @Pattern(regexp = "^[0-9]{14}$", message = "ИНН должен содержать 14 цифр")
    private String personalNumber;
    
    @NotBlank(message = "Номер паспорта обязателен!")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{7}$", message = "Формат паспорта: AN1234567")
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
