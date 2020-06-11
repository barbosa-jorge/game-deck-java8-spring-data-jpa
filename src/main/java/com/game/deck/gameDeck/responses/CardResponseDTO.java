package com.game.deck.gameDeck.responses;

import com.game.deck.gameDeck.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDTO {
    private String faceValue;
    private String suit;

    public static List<CardResponseDTO> mapToCardDTOList(List<Card> cards) {
        return cards.stream().map(card -> new CardResponseDTO(card.getFaceValue(), card.getSuit()))
                .collect(Collectors.toList());
    }
}
