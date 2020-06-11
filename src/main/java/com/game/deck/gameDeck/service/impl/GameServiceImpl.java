package com.game.deck.gameDeck.service.impl;

import com.game.deck.gameDeck.entity.Card;
import com.game.deck.gameDeck.entity.Game;
import com.game.deck.gameDeck.entity.Player;
import com.game.deck.gameDeck.exceptions.BadRequestException;
import com.game.deck.gameDeck.exceptions.GameException;
import com.game.deck.gameDeck.exceptions.NotFoundException;
import com.game.deck.gameDeck.repository.CardRepository;
import com.game.deck.gameDeck.repository.GameRepository;
import com.game.deck.gameDeck.repository.PlayerRepository;
import com.game.deck.gameDeck.requests.AddPlayerRequestDTO;
import com.game.deck.gameDeck.requests.CreateGameRequestDTO;
import com.game.deck.gameDeck.responses.CardResponseDTO;
import com.game.deck.gameDeck.responses.CardsBySuit;
import com.game.deck.gameDeck.responses.CardsBySuitAndValue;
import com.game.deck.gameDeck.responses.GameResponseDTO;
import com.game.deck.gameDeck.service.GameService;
import com.game.deck.gameDeck.shared.constants.AppErrorConstants;
import com.game.deck.gameDeck.shared.constants.GameConstants;
import com.game.deck.gameDeck.shared.utils.DeckUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private static final int FIRST_CARD = 0;

    @Autowired
    private GameRepository repository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckUtils deckUtils;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MessageSource messageSource;

    public GameResponseDTO createGame(CreateGameRequestDTO createGameRequest) {

        validateMandatoryRequestFields(createGameRequest);

        Game game = new Game();
        addPlayersToGame(game, createGameRequest.getPlayerNames());
        addCardsToGameDeck(createGameRequest.getNumberOfDecks(), game);
        Game savedGame = repository.save(game);

        return GameResponseDTO.mapEntityToDTO(savedGame);
    }

    @Override
    public void deleteGame(Long gameId) {
        validateGameExists(gameId);
        repository.deleteById(gameId);
    }

    @Override
    public GameResponseDTO getGameById(Long gameId) {
        return GameResponseDTO.mapEntityToDTO(getGameWithCardsAndPlayersById(gameId));
    }

    @Override
    public GameResponseDTO dealCards(Long gameId, Long playerId) {
        Game game = getGameWithCardsAndPlayersById(gameId);
        Player player = getPlayerFromGameById(game, playerId);
        addCardToPlayer(player, game.getGameCards());
        repository.save(game);

        return GameResponseDTO.mapEntityToDTO(game);
    }

    @Override
    public GameResponseDTO addPlayer(Long gameId, AddPlayerRequestDTO addPlayerRequestDTO) {
        String playerName = addPlayerRequestDTO.getName();
        requiredNonEmpty(playerName, GameConstants.PLAYER_NAME);

        Game game = getGameWithCardsAndPlayersById(gameId);
        validateExistentPlayer(game.getPlayers(), playerName);
        game.getPlayers().add(new Player(playerName, game));
        Game savedGame = repository.save(game);

        return GameResponseDTO.mapEntityToDTO(savedGame);
    }

    @Override
    public void deletePlayer(Long gameId, Long playerId) {
        Game game = getGameWithCardsAndPlayersById(gameId);
        Player player = getPlayerFromGameById(game, playerId);
        game.getPlayers().remove(player);
        repository.save(game);
    }

    @Override
    public List<CardResponseDTO> getPlayerCards(Long gameId, Long playerId) {
        Game game = getGameWithCardsAndPlayersById(gameId);
        Player player = getPlayerFromGameById(game, playerId);
        return CardResponseDTO.mapToCardDTOList(player.getOnHandCards());
    }

    @Override
    public GameResponseDTO addNewDeck(Long gameId) {
        Game game = getGameWithCardsAndPlayersById(gameId);
        game.getGameCards().addAll(deckUtils.createDeck(game));
        Game savedGame = repository.save(game);
        return GameResponseDTO.mapEntityToDTO(savedGame);
    }

    @Override
    public List<CardsBySuit> getCountRemainingCardsBySuit(Long gameId) {
        Game game = getOnlyGameWithId(gameId);
        return cardRepository.countCardsBySuit(game);
    }

    @Override
    public List<CardsBySuitAndValue> getCountRemainingCardsSorted(Long gameId, Sort sort) {
        Game game = getOnlyGameWithId(gameId);
        return cardRepository.countRemainingCardsSort(game, sort);
    }

    public GameResponseDTO shuffleCards(Long gameId) {
        Game game = getGameWithCardsAndPlayersById(gameId);
        deckUtils.shuffleCards(game.getGameCards());

        Game savedGame =  repository.save(game);
        return GameResponseDTO.mapEntityToDTO(savedGame);
    }

    private void requiredNonEmpty(String fieldValue, String field) {
        if (StringUtils.isEmpty(fieldValue)) {
            throw new BadRequestException(buildErrorMessage(AppErrorConstants.ERROR_FIELD_CANNOT_BE_EMPTY,
                    new Object[]{ field }));
        }
    }

    private void validateExistentPlayer(List<Player> players, String playerName) {

        boolean isPlayerFound = players.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(playerName));

        if (isPlayerFound) {
            throw new GameException(buildErrorMessage(AppErrorConstants
                    .ERROR_PLAYER_ALREADY_EXISTS, new Object[]{ playerName }));
        }
    }

    private Player getPlayerFromGameById(Game game, Long playerId) {
        return game.getPlayers().stream()
                .filter(p -> p.getId() == playerId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(buildErrorMessage(AppErrorConstants
                        .ERROR_PLAYER_NOT_FOUND, AppErrorConstants.NO_PARAMS)));
    }

    private void addCardToPlayer(Player player, List<Card> cards) {
        validateAvailableCards(cards);

        Card pickedCard = cards.remove(FIRST_CARD);
        pickedCard.setGame(null);
        pickedCard.setPlayer(player);
        player.getOnHandCards().add(pickedCard);
    }

    private void validateAvailableCards(List<Card> cards) {
        if (CollectionUtils.isEmpty(cards)) {
            throw new GameException(buildErrorMessage(AppErrorConstants
                    .ERROR_NO_MORE_CARDS_AVAILABLE, AppErrorConstants.NO_PARAMS));
        }
    }

    private Game getGameWithCardsAndPlayersById(Long gameId) {
        Game game = getGameOnlyWithCardsById(gameId);
        List<Player> gamePlayers = getAllPlayersWithOnHandCardsByGame(game);
        game.setPlayers(gamePlayers);
        return game;
    }

    private List<Player> getAllPlayersWithOnHandCardsByGame(Game game) {
        return playerRepository.findAllWithOnHandCardsByGame(game);
    }

    private Game getGameOnlyWithCardsById(Long gameId) {
        return repository.findOneWithGameCardsById(gameId)
                .orElseThrow(() -> new NotFoundException(buildErrorMessage(AppErrorConstants.ERROR_GAME_NOT_FOUND,
                        AppErrorConstants.NO_PARAMS)));
    }

    private String buildErrorMessage(String errorBundleKey, Object[] params) {
        return messageSource.getMessage(errorBundleKey, params,
                LocaleContextHolder.getLocale());
    }

    private void addPlayersToGame(Game game, List<String> playerNames) {
        List<Player> players = playerNames.stream()
                .map(playerName -> new Player(playerName, game))
                .collect(Collectors.toList());

        game.setPlayers(players);
    }

    private void addCardsToGameDeck(int numberOfDecks, Game game) {

        List<Card> gameCards = new ArrayList<>();

        for (int i = 0; i < numberOfDecks; i++) {
            gameCards.addAll(deckUtils.createDeck(game));
        }

        game.setGameCards(gameCards);
    }

    private void validateGameExists(Long gameId) {

        boolean isGameFound = repository.existsById(gameId);

        if (!isGameFound) {
            throw new NotFoundException(buildErrorMessage(AppErrorConstants.ERROR_GAME_NOT_FOUND,
                    AppErrorConstants.NO_PARAMS));
        }
    }

    private void validateMandatoryRequestFields(CreateGameRequestDTO requestDTO) {

        if (requestDTO.getNumberOfDecks() < 1) {
            throw new BadRequestException(buildErrorMessage(AppErrorConstants.ERROR_ADD_DECK_TO_GAME,
                    AppErrorConstants.NO_PARAMS));
        }

        if (CollectionUtils.isEmpty(requestDTO.getPlayerNames())) {
            throw new BadRequestException(buildErrorMessage(AppErrorConstants.ERROR_ADD_PLAYER_TO_GAME,
                    AppErrorConstants.NO_PARAMS));
        }
    }

    private Game getOnlyGameWithId(Long gameId) {
        return repository.findById(gameId)
                .orElseThrow(() -> new NotFoundException(buildErrorMessage(AppErrorConstants
                        .ERROR_GAME_NOT_FOUND, AppErrorConstants.NO_PARAMS)));
    }
}