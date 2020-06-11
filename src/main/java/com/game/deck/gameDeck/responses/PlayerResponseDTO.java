package com.game.deck.gameDeck.responses;

import com.game.deck.gameDeck.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponseDTO {
    private String name;
    private List<CardResponseDTO> onHandCards = new ArrayList<>();

    public static List<PlayerResponseDTO> mapToPlayerDTOList(List<Player> players) {
        return players.stream()
                .map(player -> new PlayerResponseDTO(player.getName(),
                        CardResponseDTO.mapToCardDTOList(player.getOnHandCards())))
                .collect(Collectors.toList());
    }
}
