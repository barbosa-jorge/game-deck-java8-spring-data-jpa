package com.game.deck.gameDeck.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPlayerRequestDTO {
    @Size(min = 3, message="{error.player.name.min.size}")
    @NotEmpty(message = "{error.player.name.not.blank}")
    private String name;
}
