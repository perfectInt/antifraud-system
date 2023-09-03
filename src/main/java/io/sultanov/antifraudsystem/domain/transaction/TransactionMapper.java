package io.sultanov.antifraudsystem.domain.transaction;

import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setIp(transactionDto.getIp());
        transaction.setDate(transactionDto.getDate());
        transaction.setNumber(transactionDto.getNumber());
        transaction.setRegion(transactionDto.getRegion());
        return transaction;
    }
}
