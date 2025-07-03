package com.example.bankcards.repository;


import com.example.bankcards.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query("SELECT t FROM Transfer t " +
            "JOIN FETCH t.fromCard fc " +
            "JOIN FETCH t.toCard tc " +
            "WHERE fc.user.id = :userId OR tc.user.id = :userId")
    Page<Transfer> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT t FROM Transfer t WHERE t.fromCard.id = :cardId OR t.toCard.id = :cardId")
    Page<Transfer> findByCardId(@Param("cardId") UUID cardId, Pageable pageable);
}