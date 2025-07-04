package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;


    @PostMapping
    //todo сделать ошибку для  localhost:8080/api/transfers post распознавания существования айди карт
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        // Извлекаем имя пользователя из UserDetails
        String username = userDetails.getUsername();
        return ResponseEntity.ok(transferService.createTransfer(transferRequest, username));
    }

    // Получить все трансферы (для админа)
    @GetMapping
    public ResponseEntity<Page<TransferResponse>> getAllTransfers(
            Pageable pageable) {
        return ResponseEntity.ok(
                transferService.getAllTransfers(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TransferResponse>> getTransfersByUser(
            @PathVariable UUID userId, Pageable pageable) {
        return ResponseEntity.ok(
                transferService.getTransfersByUser(userId, pageable));
    }
    @GetMapping("/card/{cardId}")
    public ResponseEntity<Page<TransferResponse>> getTransfersByCard(
            @PathVariable UUID cardId,
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(
                transferService.getTransfersByCard(cardId, userDetails.getUsername(), pageable)
        );
    }

    }

