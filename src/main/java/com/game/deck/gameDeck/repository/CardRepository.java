package com.game.deck.gameDeck.repository;

import com.game.deck.gameDeck.entity.Card;
import com.game.deck.gameDeck.entity.Game;
import com.game.deck.gameDeck.responses.CardsBySuit;
import com.game.deck.gameDeck.responses.CardsBySuitAndValue;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query(" SELECT new com.game.deck.gameDeck.responses.CardsBySuit(card.suit, COUNT(card.suit)) " +
            " FROM Card card " +
            " where card.game = :game " +
            " GROUP BY card.suit")
    List<CardsBySuit> countCardsBySuit(Game game);

    @Query(" SELECT new com.game.deck.gameDeck.responses.CardsBySuitAndValue(card.faceValue, card.suit, COUNT(*)) " +
            " FROM Card card " +
            " where card.game = :game " +
            " GROUP BY card.faceValue, card.suit, card.value ")
    List<CardsBySuitAndValue> countRemainingCardsSort(Game game, Sort sort);
}
