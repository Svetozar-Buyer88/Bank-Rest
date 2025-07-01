package com.example.bankcards.controller;


import com.example.bankcards.dto.AuthRequest;
import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")// войти в систему получить токе
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody(required = false) AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")// зарегаться получить токен
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody(required = false) AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerUser(request, Role.ROLE_USER));
    }

    @PostMapping("/register-admin")// регистрация админа доступна всем
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody(required = false) AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerUser(request, Role.ROLE_ADMIN));
    }
}
