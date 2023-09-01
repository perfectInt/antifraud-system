package io.sultanov.antifraudsystem.api.controllers;

import io.sultanov.antifraudsystem.domain.transaction.TransactionDto;
import io.sultanov.antifraudsystem.domain.transaction.TransactionResponse;
import io.sultanov.antifraudsystem.domain.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/antifraud")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transaction")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public TransactionResponse postTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.postTransaction(transactionDto);
    }
}
