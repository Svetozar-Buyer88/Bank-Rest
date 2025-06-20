package com.example.bankcards.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequest {
    private String cardNumber;
    private String ownerName;
    private LocalDate expiryDate;
    private BigDecimal balance;
    private UUID userId;
}
