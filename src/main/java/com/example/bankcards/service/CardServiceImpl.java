package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    Logger log = LogManager.getLogger();

    @Override
    @Transactional(readOnly = true)
    public CardResponse getCardById(UUID id, String currentUsername) {
        Card card = findCardOrThrow(id);
        checkCardAccess(card, currentUsername);
        return cardMapper.toResponse(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getUserCards(String username, Pageable pageable) {
        User user = findUserByUsernameOrThrow(username);
        return cardRepository.findByUser(user,pageable)
                .map(cardMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getUserCards(UUID userId, Pageable pageable) {
        User user = findUserByIdOrThrow(userId);
        return cardRepository.findByUser(user, pageable)
                .map(cardMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getAllCards(Pageable pageable, String currentUsername) {
        User currentUser = findUserByUsernameOrThrow(currentUsername);
        if (!currentUser.isAdmin()) {
            throw new AccessDeniedException("Only admin can view all cards");
        }
        return cardRepository.findAll(pageable)
                .map(cardMapper::toResponse);
    }

    @Override
    @Transactional
    public CardResponse createCard(CardRequest request, String currentUsername) {
        User currentUser = findUserByUsernameOrThrow(currentUsername);
        User cardUser = findUserByIdOrThrow(request.getUserId());

        if (!currentUser.getId().equals(cardUser.getId()) && !currentUser.isAdmin()) {
            throw new AccessDeniedException("Cannot create card for another user");
        }
        validateBalance(request.getBalance());

        // Очищаем номер карты от нецифровых символов
        String cleanedCardNumber = request.getCardNumber().replaceAll("\\D", "");
        request.setCardNumber(cleanedCardNumber);

if(cardRepository.existsByCardNumber(cleanedCardNumber)){
    throw new CardExistsException("Card with number ending with "
            + cleanedCardNumber.substring(cleanedCardNumber.length() - 4)
            + " already exists");
}

        Card card = buildCardFromRequest(request, cardUser);
        Card savedCard = cardRepository.save(card);
        return cardMapper.toResponse(savedCard);
    }
    @Override
    @Transactional
    public void deleteCard(UUID id, String currentUsername) {
        Card card = findCardOrThrow(id);
        checkCardAccess(card, currentUsername);
        cardRepository.delete(card);
    }



    @Override
    public CardResponse blockedCard(UUID id, String currentUsername) {
        Card card=cardRepository.findById(id).orElseThrow();
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new CardOperationException("Card is already blocked");
        }

        if (card.getStatus() == CardStatus.EXPIRED) {
            throw new CardOperationException("Cannot block expired card");
        }
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        log.info("Card {} blocked by user {}", card.getMaskedCardNumber(), currentUsername);
        return cardMapper.toResponse(card);
    }

    @Override
    @Transactional
    public CardResponse activateCard(UUID cardId, String currentUsername)  {
        Card card = findCardOrThrow(cardId);
        checkCardAccess(card, currentUsername);

        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new CardOperationException("Card is already active");
        }

        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CardOperationException("Cannot activate expired card");
        }

        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);

        log.info("Card {} activated by user {}", card.getMaskedCardNumber(), currentUsername);
        return cardMapper.toResponse(card);
    }

    private Card findCardOrThrow(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private User findUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private User findUserByIdOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private void checkCardAccess(Card card, String currentUsername) {
        User currentUser = findUserByUsernameOrThrow(currentUsername);
        if (!card.getUser().getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new AccessDeniedException("Access to card denied");
        }
    }

    private Card buildCardFromRequest(CardRequest request, User user) {
        return Card.builder()
                .cardNumber(request.getCardNumber())
                .ownerName(request.getOwnerName())
                .expiryDate(request.getExpiryDate())
                .balance(request.getBalance())
                .user(user)
                .status(determineInitialStatus(request))
                .build();
    }

    private CardStatus determineInitialStatus(CardRequest request) {
        return request.getExpiryDate().isBefore(LocalDate.now())
                ? CardStatus.EXPIRED
                : CardStatus.ACTIVE;
    }
    private void validateBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }
}