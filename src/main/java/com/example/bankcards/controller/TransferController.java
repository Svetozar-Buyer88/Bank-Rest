package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Transfer> transfer(@RequestBody TransferRequest transferRequest) {
        UUID from = transferRequest.getFromCardId();
        UUID to = transferRequest.getToCardId();
        BigDecimal amount = transferRequest.getAmount();
        System.out.println("уровень контроллера");
        Transfer tx = transferService.transfer(from, to, amount);
        return ResponseEntity.ok(tx);
    }
}
