package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

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
    public Page<CardResponse> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(cardMapper::toResponse);
    }

    @Override
    @Transactional
    public CardResponse createCard(CardRequest request, String currentUsername) {
//        User currentUser = findUserByUsernameOrThrow(currentUsername);
//
//      //   Проверка прав: только админ может создавать карты для других пользователей
//        if (!request.getUserId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
//            throw new AccessDeniedException("Only admins can create cards for other users");
//        }

        User cardUser = findUserByIdOrThrow(request.getUserId());
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

    private Card findCardOrThrow(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
    }

    private User findUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User findUserByIdOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
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
}