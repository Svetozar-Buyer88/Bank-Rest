package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final UserService userService;

    public TransferService(TransferRepository transferRepository, CardRepository cardRepository, UserService userService) {
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
        this.userService = userService;
    }

    @Transactional
    public Transfer createTransfer(UUID fromId, UUID toId, BigDecimal amount, String currentUsername) {
        User currentUser = userService.getUserByUsername(currentUsername);

        Card from = cardRepository.findById(fromId)
                .orElseThrow(() -> new CardNotFoundException("From card not found"));
        Card to = cardRepository.findById(toId)
                .orElseThrow(() -> new CardNotFoundException("To card not found"));

        // Проверка владения картой
        if (!from.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't own the source card");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        cardRepository.save(from);
        cardRepository.save(to);

        Transfer tx = new Transfer();
        tx.setFromCard(from);
        tx.setToCard(to);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        return transferRepository.save(tx);

    }

    public Page<Transfer> getAllTransfers(Pageable page){
        return transferRepository.findAll(page);
    }

    public Page<Transfer> getTransfersByUser(UUID userId,Pageable page){
        return  transferRepository.findByUserId(userId,page);
    }

}