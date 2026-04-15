package com.example.nexobank2.dto;

import com.example.nexobank2.enums.ClientStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientRequest {
    @Email(message = "Не корректный email")
    @NotBlank(message = "Это поле обязательно!")
    private String email;
    @Size(min = 10, max = 15, message = "Не корректный ввод")
    @NotBlank(message = "Это поле обязательно!")
    @Pattern(regexp = "^\\+0?[0-9]{11,15}$", message = "Номер телефона должен содержать 11-15 цифр")
    private String phoneNumber;

    @NotBlank(message = "Имя обязательно!")
    @Size(min = 2, max = 50,message = "Имя должно быть от 2 до 50 символов")
    private String firstName;
    @Size(min = 2, max = 50,message = "Имя должно быть от 2 до 50 символов")
    @NotBlank(message = "Фамилия обязательно!")
    private String lastName;
    private String middleName;      // необязательно
    @Past(message = "Некорректная дата рождения")
    @NotNull(message = "Дата рождения обязательна!")
    private LocalDate dateOfBirth;
    private String personalNumber;
    private String passportNumber;
}
