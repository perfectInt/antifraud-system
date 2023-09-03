package io.sultanov.antifraudsystem.domain.transaction;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TransactionDto {
    private Long amount;

    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    private String ip;

    @NotEmpty
    private String number;

    @NotEmpty
    private String region;

    @NotEmpty
    private String date;
}
