package com.example.nexobank2.controller;

import com.example.nexobank2.dto.recordDto.AuthResponse;
import com.example.nexobank2.dto.recordDto.LoginRequest;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.UserRepository;
import com.example.nexobank2.service.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Аутентификация", description = "API для входа в систему и получения JWT токена")
public class AuthController {
    private final AuthenticationManager manager;
    private final JwtService service;
    private final UserRepository repository;
    @PostMapping("/login")
    @Operation(
        summary = "Вход в систему",
        description = "Аутентифицирует пользователя по email и паролю, возвращает JWT токен для доступа к защищенным эндпойнтам"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Успешная аутентификация, токен выдан",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Неверный email или пароль"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public ResponseEntity<AuthResponse> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для входа (email и пароль)",
            required = true
        ) @RequestBody @Valid LoginRequest request){
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        User user = repository.findUserByEmail(request.email()).orElseThrow(()-> new NotFoundException("User not found"));
        String token = service.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
