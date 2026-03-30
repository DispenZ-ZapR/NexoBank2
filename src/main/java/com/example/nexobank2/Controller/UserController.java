package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.UserRequest;
import com.example.nexobank2.dto.UserResponse;
import com.example.nexobank2.dto.recordDto.ActivationAccount;
import com.example.nexobank2.dto.recordDto.UpdateEmail;
import com.example.nexobank2.dto.recordDto.UpdatePhoneNumber;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.UserType;
import com.example.nexobank2.mapper.UserMapper;

import com.example.nexobank2.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "Контроллер пользователей")
public class UserController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    @PostMapping("/verify/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String token, @RequestBody ActivationAccount password){
        userService.activateAccount(password.password(), token);
        return ResponseEntity.ok("Аккаунт успешно подтвержден");
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(userMapper.toResponse(userService.findById(id)));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userMapper.toUserResponseList(userService.findAll()));
    }
    @GetMapping("/getByEmail")
    public ResponseEntity<UserResponse> getByEmail(@RequestParam String email){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByEmail(email)));
    }
    @GetMapping("/getByPassportId/{passportId}")
    public ResponseEntity<UserResponse> getByPassportId(@PathVariable Long passportId){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByPassportId(passportId)));
    }
    @GetMapping("/getByPhoneNumber")
    public ResponseEntity<UserResponse> getByPhoneNumber(@RequestParam String phoneNumber){
        return ResponseEntity.ok(userMapper.toResponse(userService.findByPhoneNumber(phoneNumber)));
    }
    @GetMapping("/getByUserType")
    public ResponseEntity<List<UserResponse>> getByUserType(@RequestParam UserType userType){
        return ResponseEntity.ok(userMapper.toUserResponseList(userService.findByUsersType(userType)));
    }
    @PutMapping("/changeEmail/{id}")
    public ResponseEntity<?> changeEmail(@PathVariable Long id, @RequestBody UpdateEmail email){
        userService.changeEmail(id, email.email());
        return ResponseEntity.ok("Почта успешно изменена");
    }
    @PutMapping("/changePhoneNumber")
    public ResponseEntity<?> changePhoneNumber(@RequestParam Long id, @RequestBody UpdatePhoneNumber phoneNumber){
        userService.changePhoneNumber(id,phoneNumber.phoneNumber());
        return ResponseEntity.ok("Номер успешно изменен");
    }
}
