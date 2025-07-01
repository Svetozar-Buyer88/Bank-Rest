package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCard(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        CardResponse response = cardService.getCardById(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<CardResponse>> getMyCards(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {

        Page<CardResponse> response = cardService.getUserCards(
                userDetails.getUsername(),
                pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CardResponse>> getAllCards(Pageable pageable) {
        Page<CardResponse> response = cardService.getAllCards(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CardResponse>> getUserCards(
            @PathVariable UUID userId,
            Pageable pageable) {

        Page<CardResponse> response = cardService.getUserCards(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(
            @Valid @RequestBody CardRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        CardResponse response = cardService.createCard(request, userDetails.getUsername());
        return ResponseEntity
                .created(URI.create("/api/cards/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        cardService.deleteCard(id, userDetails.getUsername());
    }
}