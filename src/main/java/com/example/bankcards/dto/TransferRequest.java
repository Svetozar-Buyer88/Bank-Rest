package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferRequest {
    @NotNull(message = "fromCardId is required")

    private UUID fromCardId;
    @NotNull(message = "toCardId is required")
    private UUID toCardId;
    @NotNull(message = "amount is required")
    private BigDecimal amount;

    // геттеры и сеттеры обязательно
}
