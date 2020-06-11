package com.game.deck.gameDeck.repository;

import com.game.deck.gameDeck.entity.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @EntityGraph(attributePaths = "gameCards")
    Optional<Game> findOneWithGameCardsById(@Param("gameId") Long gameId);

}
