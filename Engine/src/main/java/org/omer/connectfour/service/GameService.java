package org.omer.connectfour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.GameStatus;
import org.omer.connectfour.api.model.MoveRequest;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.api.model.Position;
import org.omer.connectfour.api.model.WinnerEnum;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.repository.GameRepository;
import org.omer.connectfour.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.omer.connectfour.utils.Constants.*;

/**
 * Service encapsulating the core logic for the Connect Four game.
 * Uses Lombok for constructor injection and logging.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    /**
     * Initializes a new game.
     *
     * @return The UUID of the created game.
     */
    public UUID createGame() {
        log.info("Creating new game via service");
        return gameRepository.createGame();
    }

    /**
     * Retrieves the game state mapped to a response object.
     *
     * @param id The game UUID.
     * @return The GameResponse DTO, or null if not found.
     */
    public GameResponse getGameResponse(UUID id) {
        Game game = gameRepository.getGame(id);
        if (game == null) {
            return null;
        }
        return gameMapper.toGameResponse(game);
    }

    /**
     * Retrieves the internal game entity.
     *
     * @param id The game UUID.
     * @return The Game entity.
     */
    public Game getGame(UUID id) {
        return gameRepository.getGame(id);
    }

    /**
     * Removes a game from the repository.
     *
     * @param id The game UUID.
     */
    public void removeGame(UUID id) {
        log.debug("Removing game {}", id);
        gameRepository.removeGame(id);
    }

    /**
     * Processes a player's move and triggers the bot's response.
     *
     * @param id          The game UUID.
     * @param moveRequest The details of the move (column).
     * @return The result of the move including updated board state, or null if game
     *         not found.
     * @throws IllegalArgumentException if the move is invalid.
     */
    public MoveResponse makeMove(UUID id, MoveRequest moveRequest) {
        Game game = gameRepository.getGame(id);
        if (game == null) {
            return null;
        }

        // Validate basic move legality using public isLegal
        // Note: isLegal checks column range and if column is full
        if (!game.getBoard().isLegal(moveRequest.getColumn())) {
            log.warn("Invalid move attempt in game {} col {}", id, moveRequest.getColumn());
            throw new IllegalArgumentException("Invalid move");
        }

        // Execute Player Move
        int playerCol = moveRequest.getColumn();

        // Since getLowest is private, we must calculate the row ourselves.
        // We can inspect the board to find the lowest empty spot.
        int playerRow = game.getBoard().getLowest(playerCol);

        game.getBoard().setMove(playerCol);

        Position playerPos = new Position().row(playerRow).column(playerCol);

        int winner = game.getBoard().winner();
        if (winner == X_WON) {
            return gameMapper.createMoveResponse(game, true, playerPos, null, GameStatus.PLAYER_WON, WinnerEnum.PLAYER);
        }

        if (game.getBoard().isFull()) {
            return gameMapper.createMoveResponse(game, true, playerPos, null, GameStatus.DRAW, null);
        }

        // Execute Bot Move
        int botCol = game.getBot().makeMove(0, org.omer.connectfour.enums.Player.BOT);
        int botRow = game.getBoard().getLowest(botCol);

        game.getBoard().setMove(botCol);
        Position botPos = new Position().row(botRow).column(botCol);

        winner = game.getBoard().winner();
        if (winner == O_WON) {
            return gameMapper.createMoveResponse(game, true, playerPos, botPos, GameStatus.BOT_WON, WinnerEnum.BOT);
        }

        if (game.getBoard().isFull()) {
            return gameMapper.createMoveResponse(game, true, playerPos, botPos, GameStatus.DRAW, null);
        }

        return gameMapper.createMoveResponse(game, true, playerPos, botPos, GameStatus.PLAYER_TURN, null);
    }
}
