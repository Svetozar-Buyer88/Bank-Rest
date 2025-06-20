package com.example.bankcards.service;



import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardService {
    Card getCardById(UUID id);

    List<Card> getCardsByUser(User user);

    Card saveCard(Card card);
    void deleteCard(UUID id);

    List<Card> findAll();

    Optional<List<Card>> findAllByUserId(UUID userId);
}
