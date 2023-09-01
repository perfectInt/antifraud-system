package io.sultanov.antifraudsystem.domain.transaction;

import io.sultanov.antifraudsystem.utils.TransactionStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransactionService {

    public TransactionResponse postTransaction(TransactionDto transactionDto) {
        if (transactionDto.getAmount() < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (transactionDto.getAmount() <= 200) return new TransactionResponse(TransactionStatus.ALLOWED.toString());
        else if (transactionDto.getAmount() <= 1500) return new TransactionResponse(TransactionStatus.MANUAL_PROCESSING.toString());
        else return new TransactionResponse(TransactionStatus.PROHIBITED.toString());
    }
}
