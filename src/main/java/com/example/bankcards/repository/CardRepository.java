package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    // Карты с истекшим сроком действия
    @Query("SELECT c FROM Card c WHERE c.status = 'ACTIVE' AND c.expiryDate < :today")
    List<Card> findActiveCardsExpiringToday(LocalDate today);

    // Заблокированные карты с положительным балансом
    @Query("SELECT c FROM Card c WHERE c.status = 'BLOCKED' AND c.balance >= 0")
    List<Card> findBlockedCardsWithPositiveBalance();


    Page<Card> findByUser(User user, Pageable pageable);}