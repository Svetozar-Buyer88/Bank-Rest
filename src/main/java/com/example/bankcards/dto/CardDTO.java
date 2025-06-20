package com.example.bankcards.dto;


import com.example.bankcards.entity.CardStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardDTO(
        UUID id,
        @NotBlank String cardNumber,
        @NotBlank String ownerName,
        @NotNull LocalDate expiryDate,
        @NotNull CardStatus status,
        @DecimalMin("0.0") BigDecimal balance,
        UUID userId
) {
}