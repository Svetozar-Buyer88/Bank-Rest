package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.mapper.TransferMapper;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService transferService;
    private final TransferMapper transferMapper;

    public TransferController(TransferService transferService, TransferMapper transferMapper) {
        this.transferService = transferService;
        this.transferMapper = transferMapper;
    }

    @PostMapping
    public ResponseEntity<Transfer> transfer(@RequestBody TransferRequest transferRequest,
                                             @AuthenticationPrincipal UserDetails userDetails) {

//        String username = userDetails.getUsername();
//        UUID from = transferRequest.getFromCardId();
//        UUID to = transferRequest.getToCardId();
//        BigDecimal amount = transferRequest.getAmount();
//        System.out.println("уровень контроллера");
//        Transfer tx = transferService.transfer(from, to, amount, username);
        Transfer response = transferService.createTransfer(
                transferRequest.getFromCardId(),transferRequest.getToCardId(),transferRequest.getAmount(),
                userDetails.getUsername()
        );
        return ResponseEntity.ok(response);
    }

    // Получить все трансферы (для админа)
    @GetMapping
    public ResponseEntity<Page<Transfer>> getAllTransfers(
            Pageable pageable) {
        return ResponseEntity.ok(transferService.getAllTransfers(pageable));

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TransferResponse>> getTransfersByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Transfer> transfers = transferService.getTransfersByUser(userId, pageable);

        return ResponseEntity.ok(
                transfers.map(c->transferMapper.toResponse(c))
        );
    }
    }

