package com.game.deck.gameDeck.requests;

import lombok.Data;

import java.util.List;

@Data
public class CreateGameRequestDTO {
    private int numberOfDecks;
    private List<String> playerNames;
}
