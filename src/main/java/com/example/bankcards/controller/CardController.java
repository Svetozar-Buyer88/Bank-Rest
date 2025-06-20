package com.example.bankcards.controller;


import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCard(@PathVariable UUID id) {
        Card card = cardService.getCardById(id);
        return ResponseEntity.ok(toResponse(card));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardResponse>> getCardsByUser(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        List<CardResponse> cards = cardService.getCardsByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest request) {
        User user = userService.getUserById(request.getUserId());
        Card card = Card.builder()
                .cardNumber(request.getCardNumber())
                .ownerName(request.getOwnerName())
                .expiryDate(request.getExpiryDate())
                .status(CardStatus.ACTIVE)
                .balance(request.getBalance())
                .user(user)
                .build();
        Card saved = cardService.saveCard(card);
        return ResponseEntity.created(URI.create("/api/cards/" + saved.getId()))
                .body(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    private CardResponse toResponse(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .maskedCardNumber(card.getMaskedCardNumber())
                .ownerName(card.getOwnerName())
                .expiryDate(card.getExpiryDate())
                .status(card.getStatus())
                .balance(card.getBalance())
                .build();
    }
}
