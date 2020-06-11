package com.game.deck.gameDeck.controller;

import com.game.deck.gameDeck.requests.AddPlayerRequestDTO;
import com.game.deck.gameDeck.requests.CreateGameRequestDTO;
import com.game.deck.gameDeck.responses.CardResponseDTO;
import com.game.deck.gameDeck.responses.CardsBySuit;
import com.game.deck.gameDeck.responses.CardsBySuitAndValue;
import com.game.deck.gameDeck.responses.GameResponseDTO;
import com.game.deck.gameDeck.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/game-management/api/v1")
public class GameControllerV1 {

    @Autowired
    private GameService gameService;

    @GetMapping("/games/{game-id}")
    public ResponseEntity<GameResponseDTO> getGameById(@PathVariable("game-id") Long gameId) {
        return new ResponseEntity(gameService.getGameById(gameId), HttpStatus.OK);
    }

    @PostMapping("/games")
    public ResponseEntity<GameResponseDTO> createGame(@Valid @RequestBody CreateGameRequestDTO request) {
        return new ResponseEntity(gameService.createGame(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/games/{game-id}")
    public ResponseEntity deleteGame(@PathVariable("game-id") Long gameId) {
        gameService.deleteGame(gameId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/games/{game-id}/players/{player-id}/deal-cards")
    public ResponseEntity<GameResponseDTO> dealCards(@PathVariable("game-id") Long gameId,
                                                     @PathVariable("player-id") Long playerId) {
        return new ResponseEntity<>(gameService.dealCards(gameId, playerId), HttpStatus.OK);
    }

    @PostMapping("/games/{game-id}/players")
    public ResponseEntity<GameResponseDTO> addPlayer(@PathVariable("game-id") Long gameId,
                                                     @Valid @RequestBody AddPlayerRequestDTO addPlayerRequestDTO) {
        return new ResponseEntity(gameService.addPlayer(gameId, addPlayerRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/games/{game-id}/players/{player-id}")
    public ResponseEntity deletePlayer(@PathVariable("game-id") Long gameId,
                                       @PathVariable("player-id") Long playerId) {
        gameService.deletePlayer(gameId, playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/games/{game-id}/players/{player-id}/cards")
    public ResponseEntity<List<CardResponseDTO>> getPlayerCards(@PathVariable("game-id") Long gameId,
                                                                @PathVariable("player-id") Long playerId) {
        return new ResponseEntity(gameService.getPlayerCards(gameId, playerId), HttpStatus.OK);
    }

    @PostMapping("/games/{game-id}/cards")
    public ResponseEntity<GameResponseDTO> addNewDeck(@PathVariable("game-id") Long gameId) {
        return new ResponseEntity(gameService.addNewDeck(gameId), HttpStatus.CREATED);
    }

    @GetMapping("/games/{game-id}/cards/remaining-suits")
    public ResponseEntity<List<CardsBySuit>> getCountRemainingCardsBySuit(@PathVariable("game-id") Long gameId) {
        return new ResponseEntity(gameService.getCountRemainingCardsBySuit(gameId), HttpStatus.OK);
    }

    @GetMapping("/games/{game-id}/cards/remaining-suits-value")
    public ResponseEntity<List<CardsBySuitAndValue>> getCountRemainingCardsSorted(@PathVariable("game-id") Long gameId,
                                                                                  Sort sort) {
        return new ResponseEntity(gameService.getCountRemainingCardsSorted(gameId, sort), HttpStatus.OK);
    }

    @PutMapping("/games/{game-id}/cards/shuffle")
    public ResponseEntity<GameResponseDTO> shuffleCards(@PathVariable("game-id") Long gameId) {
        return new ResponseEntity(gameService.shuffleCards(gameId), HttpStatus.OK);
    }
}
