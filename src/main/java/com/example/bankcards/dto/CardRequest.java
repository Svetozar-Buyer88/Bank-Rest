package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequest {
    @NotBlank(message = "cardNumber is required")
    private String cardNumber;
    @NotBlank(message = "ownerName is required")
    private String ownerName;
    @NotNull(message = "expiryDate is required")
    private LocalDate expiryDate;
    @NotNull(message = "balance is required")
    private BigDecimal balance;
    @NotNull(message = "userId is required")
    private UUID userId;
}
