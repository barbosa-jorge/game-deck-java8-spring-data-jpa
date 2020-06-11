package com.game.deck.gameDeck.repository;

import com.game.deck.gameDeck.entity.Game;
import com.game.deck.gameDeck.entity.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @EntityGraph(attributePaths = "onHandCards")
    List<Player> findAllWithOnHandCardsByGame(Game game);
}
