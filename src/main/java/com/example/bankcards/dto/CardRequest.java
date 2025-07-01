package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^[0-9\\s\\-]{16,22}$", message = "Invalid card number format")
    private String cardNumber;

    @NotBlank(message = "ownerName is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Owner name must contain only letters and spaces")
    private String ownerName;

    @NotNull(message = "expiryDate is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @NotNull(message = "balance is required")
    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    @Digits(integer = 12, fraction = 2, message = "Balance must have up to 12 integer and 2 fraction digits")
    private BigDecimal balance;

    @NotNull(message = "userId is required")
    private UUID userId;
}