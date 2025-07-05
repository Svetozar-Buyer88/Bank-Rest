package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.exception.CardOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CardService  {
    CardResponse getCardById(UUID id, String currentUsername);
    Page<CardResponse> getUserCards(String username, Pageable pageable);
    Page<CardResponse> getUserCards(UUID userId, Pageable pageable);
    Page<CardResponse> getAllCards(Pageable page, String currentUsername);
    CardResponse createCard(CardRequest request, String currentUsername);
    void deleteCard(UUID id, String currentUsername);


    CardResponse blockedCard(UUID id, String currentUsername) throws CardOperationException;

    @Transactional
    CardResponse activateCard(UUID cardId, String currentUsername) throws CardOperationException;
}