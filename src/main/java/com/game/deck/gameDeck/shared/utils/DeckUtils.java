package com.game.deck.gameDeck.shared.utils;

import com.game.deck.gameDeck.entity.Card;
import com.game.deck.gameDeck.entity.Game;
import com.game.deck.gameDeck.shared.CardEnum;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeckUtils {

    public List<Card> createDeck(Game game) {
        List<Card> cards = Arrays.stream(CardEnum.values())
                .map(c -> new Card(null, game, null,  c.getValue(), c.getSuit(), c.getFaceValue()))
                .collect(Collectors.toList());
        return cards;
    }

    public void shuffleCards(List<Card> cards) {

        if (CollectionUtils.isEmpty(cards)) {
            return;
        }

        for (int i = 0; i < cards.size(); i++) {
            int index = (int) (Math.random() * cards.size());
            Card currentCard = cards.get(i);
            Card movedCard = cards.get(index);
            cards.set(index, currentCard);
            cards.set(i, movedCard);
        }
    }
}
