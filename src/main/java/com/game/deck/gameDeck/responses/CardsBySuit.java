package com.game.deck.gameDeck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardsBySuit {
    private String suit;
    private long value;
}
