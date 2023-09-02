package io.sultanov.antifraudsystem.api.controllers;

import io.sultanov.antifraudsystem.domain.card.Card;
import io.sultanov.antifraudsystem.domain.card.CardDto;
import io.sultanov.antifraudsystem.domain.card.CardService;
import io.sultanov.antifraudsystem.domain.card.DeleteCardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/antifraud/stolencard")
@PreAuthorize("hasAuthority('SUPPORT')")
public class CardController {

    private final CardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Card addStolenCard(@Valid @RequestBody CardDto cardDto) {
        return cardService.addStolenCard(cardDto);
    }

    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteCardResponse deleteCard(@PathVariable String number) {
        return cardService.deleteCard(number);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Card> getListOfStolenCards() {
        return cardService.getAllStolenCards();
    }
}
