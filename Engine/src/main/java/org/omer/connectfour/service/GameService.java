package org.omer.connectfour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.GameStatus;
import org.omer.connectfour.api.model.MoveRequest;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.api.model.Position;
import org.omer.connectfour.bot.Bot;
import org.omer.connectfour.exception.GameNotFoundException;
import org.omer.connectfour.exception.IllegalMoveException;
import org.omer.connectfour.model.Board;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.repository.GameRepository;
import org.omer.connectfour.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        log.info("Creating new game");

        Board board = new Board(Constants.ROWS, Constants.COLS);
        Bot bot = new Bot(board, O_ON_BOARD);
        Game game = new Game(board, bot);

        gameRepository.save(game);
        log.info("Game {} created successfully", game.getUuid());

        return game.getUuid();
    }

    /**
     * Retrieves the game state mapped to a response object.
     *
     * @param id The game UUID.
     * @return The GameResponse.
     * @throws GameNotFoundException if the game does not exist.
     */
    public GameResponse getGameResponse(UUID id) {
        Game game = findGameOrThrow(id);

        return new GameResponse()
                .id(game.getUuid())
                .board(gameMapper.mapBoard(game.getBoard().getBoard()))
                .status(gameMapper.determineGameStatus(game));
    }

    /**
     * Removes a game from the repository.
     *
     * @param id The game UUID.
     * @throws GameNotFoundException if the game does not exist.
     */
    public void removeGame(UUID id) {
        if (!gameRepository.existsById(id)) {
            throw new GameNotFoundException(id);
        }

        log.debug("Removing game {}", id);
        gameRepository.deleteById(id);
    }

    /**
     * Processes a player's move and triggers the bot's response.
     *
     * @param id          The game UUID.
     * @param moveRequest The details of the move (column).
     * @return The MoveResponse with bot's move and game status.
     * @throws GameNotFoundException if the game does not exist.
     * @throws IllegalMoveException  if the move is invalid.
     */
    public MoveResponse makeMove(UUID id, MoveRequest moveRequest) {
        Game game = findGameOrThrow(id);
        validateMove(id, game.getBoard(), moveRequest.getColumn());

        return processTurn(game, moveRequest.getColumn());
    }

    private Game findGameOrThrow(UUID id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    private void validateMove(UUID id, Board board, int column) {
        if (column < 0 || column >= board.getCols()) {
            log.error("Invalid move in game {}: column {} does not exist", id, column);
            throw new IllegalMoveException(
                    "Column " + column + " does not exist",
                    IllegalMoveException.Reason.COLUMN_NOT_FOUND);
        }

        if (board.getLowest(column) == -1) {
            log.warn("Invalid move in game {}: column {} is full", id, column);
            throw new IllegalMoveException(
                    "Column " + column + " is full",
                    IllegalMoveException.Reason.COLUMN_FULL);
        }
    }

    private MoveResponse processTurn(Game game, int playerCol) {
        Board board = game.getBoard();

        // Player move
        executeMove(board, playerCol);

        if (board.winner() == X_WON) {
            return new MoveResponse().status(GameStatus.PLAYER_WON);
        }

        if (board.isFull()) {
            return new MoveResponse().status(GameStatus.DRAW);
        }

        // Bot move
        int botCol = game.getBot().makeMove(0, org.omer.connectfour.enums.Player.BOT);
        Position botPos = executeMove(board, botCol);

        return new MoveResponse().botMove(botPos).status(gameMapper.determineGameStatus(game));
    }

    private Position executeMove(Board board, int column) {
        int row = board.getLowest(column);
        board.setMove(column);

        return new Position().row(row).column(column);
    }
}
