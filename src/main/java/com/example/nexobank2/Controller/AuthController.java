package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.recordDto.AuthResponse;
import com.example.nexobank2.dto.recordDto.LoginRequest;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.exception.NotFoundException;
import com.example.nexobank2.repository.UserRepository;
import com.example.nexobank2.service.impl.JwtService;
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
@Tag(name = "Auth")
public class AuthController {
    private final AuthenticationManager manager;
    private final JwtService service;
    private final UserRepository repository;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request){
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        User user = repository.findUserByEmail(request.email()).orElseThrow(()-> new NotFoundException("User not found"));
        String token = service.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
