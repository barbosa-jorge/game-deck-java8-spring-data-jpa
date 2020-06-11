package com.game.deck.gameDeck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardsBySuitAndValue {
    private String faceValue;
    private String suit;
    private long total;
}
