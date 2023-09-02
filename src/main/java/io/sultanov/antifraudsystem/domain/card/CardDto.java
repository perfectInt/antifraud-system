package io.sultanov.antifraudsystem.domain.card;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @NotEmpty
    private String number;
}
