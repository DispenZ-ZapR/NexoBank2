package com.example.nexobank2.controller;

import com.example.nexobank2.dto.UserResponse;
import com.example.nexobank2.dto.recordDto.ActivationAccount;
import com.example.nexobank2.dto.recordDto.AuthResponse;
import com.example.nexobank2.dto.recordDto.UpdateEmail;
import com.example.nexobank2.dto.recordDto.UpdatePhoneNumber;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.UserType;
import com.example.nexobank2.mapper.UserMapper;

import com.example.nexobank2.service.UserService;
import com.example.nexobank2.service.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями системы")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    @PostMapping("/verify/{token}")
    @Operation(
        summary = "Активировать аккаунт",
        description = "Подтверждает регистрацию пользователя по токену из email и устанавливает пароль. Возвращает JWT токен"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Аккаунт успешно активирован, токен выдан"),
        @ApiResponse(responseCode = "400", description = "Неверный токен или токен истек"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<?> activateAccount(
        @Parameter(description = "Токен активации из email", required = true) @PathVariable String token,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Пароль для аккаунта",
            required = true
        ) @RequestBody @Valid ActivationAccount password){
        User user = userService.findByActivationToken(token);
        userService.activateAccount(password.password(), token);
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
    @GetMapping("/getById/{id}")
    @Operation(
        summary = "Получить пользователя по ID",
        description = "Возвращает детальную информацию о пользователе"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getById(
        @Parameter(description = "ID пользователя", required = true) @PathVariable @Min(1) Long id){
        return ResponseEntity.ok(userMapper.toResponse(userService.findById(id)));
    }
    @GetMapping("/all")
    @Operation(
        summary = "Получить всех пользователей",
        description = "Возвращает постраничный список всех пользователей системы"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список пользователей получен")
    })
    public ResponseEntity<?> getAll(
        @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(userMapper.toUserResponseList(userService.findAll(page,size)));
    }
    @GetMapping("/getByEmail")
    @Operation(
        summary = "Найти пользователя по email",
        description = "Возвращает пользователя по адресу электронной почты"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getByEmail(
        @Parameter(description = "Email пользователя", required = true) @RequestParam @NotBlank String email){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByEmail(email)));
    }
    @GetMapping("/getByPassportId/{passportId}")
    @Operation(
        summary = "Найти пользователя по ID паспорта",
        description = "Возвращает пользователя по идентификатору паспорта"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getByPassportId(
        @Parameter(description = "ID паспорта", required = true) @PathVariable @Min(1) Long passportId){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByPassportId(passportId)));
    }
    @GetMapping("/getByPhoneNumber")
    @Operation(
        summary = "Найти пользователя по номеру телефона",
        description = "Возвращает пользователя по номеру телефона"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getByPhoneNumber(
        @Parameter(description = "Номер телефона", required = true) @RequestParam @NotBlank String phoneNumber){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByPhoneNumber(phoneNumber)));
    }
    @GetMapping("/getByUserType")
    @Operation(
        summary = "Получить пользователей по типу",
        description = "Возвращает список пользователей с указанным типом (CLIENT, EMPLOYEE)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список пользователей получен")
    })
    public ResponseEntity<List<UserResponse>> getByUserType(
        @Parameter(description = "Тип пользователя", required = true) @RequestParam @NotNull UserType userType){
        return ResponseEntity.ok(userMapper.toUserResponseList(userService.findByUsersType(userType)));
    }
    @PutMapping("/changeEmail/{id}")
    @Operation(
        summary = "Изменить email пользователя",
        description = "Обновляет адрес электронной почты пользователя"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Email успешно изменен"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
        @ApiResponse(responseCode = "409", description = "Email уже используется")
    })
    public ResponseEntity<?> changeEmail(
        @Parameter(description = "ID пользователя", required = true) @PathVariable @Min(1) Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Новый email",
            required = true
        ) @RequestBody @Valid UpdateEmail email){
        userService.changeEmail(id, email.email());
        return ResponseEntity.ok("Почта успешно изменена");
    }
    @PutMapping("/changePhoneNumber")
    @Operation(
        summary = "Изменить номер телефона",
        description = "Обновляет номер телефона пользователя"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Номер успешно изменен"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
        @ApiResponse(responseCode = "409", description = "Номер уже используется")
    })
    public ResponseEntity<?> changePhoneNumber(
        @Parameter(description = "ID пользователя", required = true) @RequestParam @Min(1) Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Новый номер телефона",
            required = true
        ) @RequestBody @Valid UpdatePhoneNumber phoneNumber){
        userService.changePhoneNumber(id,phoneNumber.phoneNumber());
        return ResponseEntity.ok("Номер успешно изменен");
    }
}
