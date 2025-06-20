package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Component;
@Component
public class CardMapper {

    public CardDTO toDto(Card card) {
        return new CardDTO(
                card.getId(),
                card.getCardNumber(),
                card.getOwnerName(),
                card.getExpiryDate(),
                card.getStatus(),
                card.getBalance(),
                card.getUser() != null ? card.getUser().getId() : null
        );
    }
    public Card toEntity(CardDTO dto, User user) {
        return Card.builder()
                .id(dto.id())
                .cardNumber(dto.cardNumber())
                .ownerName(dto.ownerName())
                .expiryDate(dto.expiryDate())
                .status(dto.status())
                .balance(dto.balance())
                .user(user)
                .build();
    }
}