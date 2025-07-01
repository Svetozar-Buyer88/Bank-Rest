package com.example.bankcards.controller;


import com.example.bankcards.dto.AuthRequest;
import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody(required = false) AuthRequest request) {

        AuthResponse response = authService.registerUser(request, Role.ROLE_USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

//        User user = User.builder()
//                .username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .roles(Collections.singleton(Role.ROLE_USER))
//                .build();
//        userRepository.save(user);
//
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(), request.getPassword()
//                )
//        );
//        String token = jwtProvider.generateToken(auth);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new AuthResponse(token));

    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody(required = false) AuthRequest request) {
        // Проверка на отсутствие тела запроса
//        if (request == null || request.getUsername() == null || request.getPassword() == null) {
//            return ResponseEntity.badRequest()
//                    .body(new AuthResponse("Request body is invalid"));
//        }
//
//        if (userRepository.existsByUsername(request.getUsername())) {
//            return ResponseEntity.status(CONFLICT)
//                    .body(new AuthResponse("Username is already taken"));
//        }
//
//        User admin = User.builder()
//                .username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .roles(Collections.singleton(Role.ROLE_ADMIN))
//                .build();
//        userRepository.save(admin);
//        String token = jwtProvider.generateToken(
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//                )
//        );
//        return ResponseEntity.ok(new AuthResponse(token));


        AuthResponse response = authService.registerUser(request, Role.ROLE_ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
