package org.omer.connectfour.repository;

import org.omer.connectfour.utils.CONSTANTS;
import org.omer.connectfour.bot.Bot;
import org.omer.connectfour.model.Board;
import org.omer.connectfour.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.omer.connectfour.utils.CONSTANTS.O_ON_BOARD;

@Repository
public class GameRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRepository.class);

    private final Map<UUID, Game> games = new HashMap<>();

    public Set<UUID> getAllGames() {
        return games.keySet();
    }

    public UUID createGame() {
        Board board = new Board(CONSTANTS.ROWS, CONSTANTS.COLS);
        Bot bot = new Bot(board, O_ON_BOARD);
        Game game = new Game(board, bot);
        games.put(game.getUuid(), game);
        return game.getUuid();
    }

    public int setMove(UUID uuid, int move) {
        return games.get(uuid).playMove(move);
    }

    public char[][] getBoard(UUID uuid) {
        return games.get(uuid).getBoard().getBoard();
    }
}
