package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber; // Храним зашифрованным, отображаем маской

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public String getMaskedCardNumber() {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
