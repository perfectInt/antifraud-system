package io.sultanov.antifraudsystem.domain.transaction;

import io.sultanov.antifraudsystem.api.component.LuhnCheck;
import io.sultanov.antifraudsystem.domain.card.CardRepository;
import io.sultanov.antifraudsystem.domain.ip.IpRepository;
import io.sultanov.antifraudsystem.utils.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private List<String> errors = new ArrayList<>();
    String transactionResult = "";
    private final CardRepository cardRepository;
    private final IpRepository ipRepository;
    private final LuhnCheck luhnCheck;

    public TransactionResponse postTransaction(TransactionDto transactionDto) {
        TransactionResponse response = new TransactionResponse();
        errors.clear();

        addErrorIfCardInvalid(transactionDto.getNumber());
        addErrorIfIpInvalid(transactionDto.getIp());
        addErrorIfAmountInvalid(transactionDto.getAmount());

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
