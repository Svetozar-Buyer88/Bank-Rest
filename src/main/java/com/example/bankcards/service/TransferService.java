package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;

    public TransferService(TransferRepository transferRepository, CardRepository cardRepository) {
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Transfer transfer(UUID fromId, UUID toId, BigDecimal amount) {
        Card from = cardRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("From card not found"));
        Card to = cardRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("To card not found"));

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
}