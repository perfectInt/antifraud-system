package io.sultanov.antifraudsystem.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionByDateAfterAndNumber(String date, String number);

    List<Transaction> findTransactionByDateAfterAndIp(String date, String ip);
}
