package io.sultanov.antifraudsystem.domain.card;

import io.sultanov.antifraudsystem.api.component.LuhnCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final LuhnCheck luhnCheck;

    public Card addStolenCard(CardDto cardDto) {
        if (!luhnCheck.isValidLuhn(cardDto.getNumber())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (cardRepository.findByNumber(cardDto.getNumber()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT);
        return cardRepository.save(cardMapper.toEntity(cardDto));
    }

    public DeleteCardResponse deleteCard(String number) {
        if (!luhnCheck.isValidLuhn(number)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Card card = cardRepository.findByNumber(number).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        cardRepository.deleteById(card.getId());
        return new DeleteCardResponse("Card " + card.getNumber() + " successfully removed!");
    }

    public List<Card> getAllStolenCards() {
        return cardRepository.findAll();
    }
}
