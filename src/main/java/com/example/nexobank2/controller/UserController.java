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
@Tag(name = "Контроллер пользователей")
@Validated
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    @PostMapping("/verify/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String token, @RequestBody @Valid ActivationAccount password){
        User user = userService.findByActivationToken(token);
        userService.activateAccount(password.password(), token);
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(userMapper.toResponse(userService.findById(id)));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(userMapper.toUserResponseList(userService.findAll(page,size)));
    }
    @GetMapping("/getByEmail")
    public ResponseEntity<UserResponse> getByEmail(@RequestParam @NotBlank String email){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByEmail(email)));
    }
    @GetMapping("/getByPassportId/{passportId}")
    public ResponseEntity<UserResponse> getByPassportId(@PathVariable @Min(1) Long passportId){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByPassportId(passportId)));
    }
    @GetMapping("/getByPhoneNumber")
    public ResponseEntity<UserResponse> getByPhoneNumber(@RequestParam @NotBlank String phoneNumber){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByPhoneNumber(phoneNumber)));
    }
    @GetMapping("/getByUserType")
    public ResponseEntity<List<UserResponse>> getByUserType(@RequestParam @NotNull UserType userType){
        return ResponseEntity.ok(userMapper.toUserResponseList(userService.findByUsersType(userType)));
    }
    @PutMapping("/changeEmail/{id}")
    public ResponseEntity<?> changeEmail(@PathVariable @Min(1) Long id, @RequestBody @Valid UpdateEmail email){
        userService.changeEmail(id, email.email());
        return ResponseEntity.ok("Почта успешно изменена");
    }
    @PutMapping("/changePhoneNumber")
    public ResponseEntity<?> changePhoneNumber(@RequestParam @Min(1) Long id, @RequestBody @Valid UpdatePhoneNumber phoneNumber){
        userService.changePhoneNumber(id,phoneNumber.phoneNumber());
        return ResponseEntity.ok("Номер успешно изменен");
    }
}
