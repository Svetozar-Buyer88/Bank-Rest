package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public Card getCardById(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Карта не найдена"));
    }

    @Override
    public List<Card> getCardsByUser(User user) {
        return cardRepository.findByUser(user);
    }

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(UUID id) {
        cardRepository.deleteById(id);
    }

}

