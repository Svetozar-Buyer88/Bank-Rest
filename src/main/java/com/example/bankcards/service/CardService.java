package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CardService  {
    CardResponse getCardById(UUID id, String currentUsername);
    Page<CardResponse> getUserCards(String username, Pageable pageable);
    Page<CardResponse> getUserCards(UUID userId, Pageable pageable);
    Page<CardResponse> getAllCards(Pageable pageable);
    CardResponse createCard(CardRequest request, String currentUsername);
    void deleteCard(UUID id, String currentUsername);
}