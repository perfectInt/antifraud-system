package io.sultanov.antifraudsystem.domain.transaction;

import io.sultanov.antifraudsystem.api.component.LuhnCheck;
import io.sultanov.antifraudsystem.domain.card.CardRepository;
import io.sultanov.antifraudsystem.domain.ip.IpRepository;
import io.sultanov.antifraudsystem.utils.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private List<String> errors = new ArrayList<>();
    String transactionResult = "";
    private final CardRepository cardRepository;
    private final IpRepository ipRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final LuhnCheck luhnCheck;

    public TransactionResponse postTransaction(TransactionDto transactionDto) {
        TransactionResponse response = new TransactionResponse();
        errors.clear();

        addErrorIfCardInvalid(transactionDto.getNumber());
        addErrorIfIpInvalid(transactionDto.getIp());
        addErrorIfAmountInvalid(transactionDto.getAmount());
        addErrorIfIpCorrelation(transactionDto.getDate(), transactionDto.getIp());
        addErrorIfDifferentRegions(transactionDto.getDate(), transactionDto.getNumber());

        transactionRepository.save(transactionMapper.toEntity(transactionDto));
        response.setResult(transactionResult);
        if (!errors.isEmpty())
            response.setInfo(getErrorInfo());
        else
            response.setInfo("none");
        return response;
    }

    private void addErrorIfCardInvalid(String number) {
        if (!luhnCheck.isValidLuhn(number)) {
            errors.add("card-number");
            transactionResult = TransactionStatus.PROHIBITED.name();
        } else if (cardRepository.findByNumber(number).isPresent()) {
            errors.add("card-number");
            transactionResult = TransactionStatus.PROHIBITED.name();
        }
    }

    private void addErrorIfIpCorrelation(String date, String ip) {
        LocalDateTime time = LocalDateTime.parse(date);
        time = time.minusHours(1);
        List<Transaction> transactions = transactionRepository.findTransactionByDateAfterAndIp(time.toString(), ip);
        int count = 0;
        Transaction initialTransaction = transactions.get(0);
        for (Transaction transaction : transactions) {
            if (!Objects.equals(transaction.getIp(), initialTransaction.getIp())) count++;
        }

        if (count > 2) {
            transactionResult = TransactionStatus.PROHIBITED.name();
            errors.add("ip-correlation");
        } else if (count == 2) {
            if (!Objects.equals(transactionResult, TransactionStatus.PROHIBITED.name())) {
                transactionResult = TransactionStatus.MANUAL_PROCESSING.name();
            }
            errors.add("ip-correlation");
        }
    }

    private void addErrorIfDifferentRegions(String date, String number) {
        LocalDateTime time = LocalDateTime.parse(date);
        time = time.minusHours(1);
        List<Transaction> transactions = transactionRepository.findTransactionByDateAfterAndNumber(time.toString(), number);
        Transaction initialTransaction = transactions.get(0);
        int count = 0;
        for (Transaction transaction : transactions) {
            if (!Objects.equals(transaction.getRegion(), initialTransaction.getRegion())) count++;
        }

        if (count > 2) {
            transactionResult = TransactionStatus.PROHIBITED.name();
            errors.add("region-correlation");
        } else if (count == 2) {
            if (!Objects.equals(transactionResult, TransactionStatus.PROHIBITED.name())) {
                transactionResult = TransactionStatus.MANUAL_PROCESSING.name();
            }
            errors.add("region-correlation");
        }
    }

    private void addErrorIfIpInvalid(String ip) {
        if (ipRepository.findByIp(ip).isPresent()) {
            errors.add("ip");
            transactionResult = TransactionStatus.PROHIBITED.name();
        }
    }

    private void addErrorIfAmountInvalid(Long amount) {
        if (amount <= 200) {
            transactionResult = TransactionStatus.ALLOWED.name();
        } else if (amount > 1500) {
            transactionResult = TransactionStatus.PROHIBITED.name();
            errors.add("amount");
        } else {
            transactionResult = TransactionStatus.MANUAL_PROCESSING.name();
            if (errors.isEmpty())
                errors.add("amount");
            else
                transactionResult = TransactionStatus.PROHIBITED.name();
        }
    }

    private String getErrorInfo() {
        StringBuilder info = new StringBuilder();
        errors.sort((String::compareToIgnoreCase));
        info.append(errors.get(0));
        for (int i = 1; i < errors.size(); i++) {
            info.append(", ").append(errors.get(i));
        }
        return info.toString();
    }
}
