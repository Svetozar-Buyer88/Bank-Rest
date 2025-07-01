package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/{id}")// получение карты по айди
    public ResponseEntity<CardResponse> getCard(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cardService.getCardById(id, userDetails.getUsername()));
    }

    @GetMapping("/my")// получение свои карт пользователем
    public ResponseEntity<Page<CardResponse>> getMyCards(
            @AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        return ResponseEntity.ok(cardService.getUserCards(
                userDetails.getUsername(),
                pageable
        ));
    }

    @GetMapping// получить все карты
    public ResponseEntity<Page<CardResponse>> getAllCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @GetMapping("/user/{userId}")// получить все карты пользователя по айди
    public ResponseEntity<Page<CardResponse>> getUserCards(
            @PathVariable UUID userId, Pageable pageable) {
        return ResponseEntity.ok(cardService.getUserCards(userId, pageable));
    }

    @PostMapping// создать карту(может или админ для всех пользователей
    // или пользователь только для себя
    public ResponseEntity<CardResponse> createCard(
            @Valid @RequestBody CardRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        CardResponse response = cardService.createCard(request, userDetails.getUsername());
        return ResponseEntity
                .created(URI.create("/api/cards/" + response.getId()))
                .body(response);
    }
    @DeleteMapping("/{id}")//удаление карты
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        cardService.deleteCard(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

}