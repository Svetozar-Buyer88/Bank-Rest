package com.example.bankcards.service;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.mapper.TransferMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {

    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final TransferMapper transferMapper;

    public TransferResponse createTransfer(TransferRequest request, String currentUsername) {
        // Проверка на перевод самому себе
        if (request.getFromCardId().equals(request.getToCardId())) {
            throw new IllegalArgumentException("Cannot transfer to the same card");
        }

        User currentUser = userService.getUserByUsername(currentUsername);
        Card fromCard = findCardAndCheckAccess(request.getFromCardId(), currentUser);
        Card toCard = findCardOrThrow(request.getToCardId());

        // Проверка статуса карт
        validateCardStatus(fromCard, toCard);

        // Проверка баланса
        if (fromCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Выполнение перевода с блокировкой
        return performTransfer(fromCard, toCard, request.getAmount());
    }

    private TransferResponse performTransfer(Card fromCard, Card toCard, BigDecimal amount) {
        // Оптимистичная блокировка через версию
        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        Transfer transfer = new Transfer();
        transfer.setFromCard(fromCard);
        transfer.setToCard(toCard);
        transfer.setAmount(amount);
        transfer.setTimestamp(LocalDateTime.now());

        Transfer savedTransfer = transferRepository.save(transfer);
        return transferMapper.toResponse(savedTransfer);
    }

    private Card findCardAndCheckAccess(UUID cardId, User currentUser) {
        Card card = findCardOrThrow(cardId);
        if (!card.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't own the source card");
        }
        return card;
    }

    private Card findCardOrThrow(UUID cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private void validateCardStatus(Card... cards) {
        for (Card card : cards) {
            if (card.getStatus() != CardStatus.ACTIVE) {
                throw new IllegalStateException("Card " + card.getId() + " is not active");
            }
        }
    }

    public Page<TransferResponse> getAllTransfers(Pageable pageable) {
        return transferRepository.findAll(pageable)
                .map(transferMapper::toResponse);
    }

    public Page<TransferResponse> getTransfersByUser(UUID userId, Pageable pageable) {
        return transferRepository.findByUserId(userId, pageable)
                .map(transferMapper::toResponse);
    }
}