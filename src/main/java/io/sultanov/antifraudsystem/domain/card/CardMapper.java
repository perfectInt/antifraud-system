package io.sultanov.antifraudsystem.domain.card;

import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public Card toEntity(CardDto cardDto) {
        Card card = new Card();
        card.setNumber(card.getNumber());
        return card;
    }
}
