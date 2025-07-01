package com.example.bankcards.repository;


import com.example.bankcards.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query("SELECT t FROM Transfer t WHERE t.fromCard.user.id = :userId OR t.toCard.user.id = :userId")
    Page<Transfer> findByUserId(@Param("userId") UUID userId, Pageable page);
   // Page<Transfer> findAll(Pageable pageable);
    public Page<Transfer> findAll(Pageable page);
}