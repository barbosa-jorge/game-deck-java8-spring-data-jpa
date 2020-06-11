package com.game.deck.gameDeck.service;

import com.game.deck.gameDeck.requests.AddPlayerRequestDTO;
import com.game.deck.gameDeck.requests.CreateGameRequestDTO;
import com.game.deck.gameDeck.responses.CardResponseDTO;
import com.game.deck.gameDeck.responses.CardsBySuit;
import com.game.deck.gameDeck.responses.CardsBySuitAndValue;
import com.game.deck.gameDeck.responses.GameResponseDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface GameService {
    GameResponseDTO createGame(CreateGameRequestDTO requestDTO);
    void deleteGame(Long gameId);
    GameResponseDTO dealCards(Long gameId, Long playerId);
    GameResponseDTO addPlayer(Long gameId, AddPlayerRequestDTO addPlayerRequestDTO);
    void deletePlayer(Long gameId, Long playerId);
    GameResponseDTO addNewDeck(Long gameId);
    List<CardResponseDTO> getPlayerCards(Long gameId, Long playerId);
    GameResponseDTO getGameById(Long gameId);
    List<CardsBySuitAndValue> getCountRemainingCardsSorted(Long gameId, Sort sort);
    List<CardsBySuit> getCountRemainingCardsBySuit(Long gameId);
    GameResponseDTO shuffleCards(Long gameId);
}