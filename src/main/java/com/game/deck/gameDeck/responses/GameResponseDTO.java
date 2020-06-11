package com.game.deck.gameDeck.responses;

import com.game.deck.gameDeck.entity.Game;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameResponseDTO {
    private Long gameId;
    private List<CardResponseDTO> gameCards = new ArrayList<>();
    private List<PlayerResponseDTO> players = new ArrayList<>();

    public static GameResponseDTO mapEntityToDTO(Game game) {
        GameResponseDTO response = new GameResponseDTO();
        response.setGameId(game.getId());
        response.setGameCards(CardResponseDTO.mapToCardDTOList(game.getGameCards()));
        response.setPlayers(PlayerResponseDTO.mapToPlayerDTOList(game.getPlayers()));
        return response;
    }
}
