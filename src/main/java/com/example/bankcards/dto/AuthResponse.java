package com.example.bankcards.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private final String tokenType = "Bearer";
}

