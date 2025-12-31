package org.omer.connectfour.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.omer.connectfour.api.model.BoardCellEnum;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.GameStatus;
import org.omer.connectfour.bot.Bot;
import org.omer.connectfour.model.Board;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.utils.Constants;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class GameMapperTest {

    private final GameMapper mapper = new GameMapper();

    @Test
    @DisplayName("Should map empty game to PLAYER_TURN status")
    void shouldMapEmptyGame() {
        Board board = new Board(Constants.ROWS, Constants.COLS);
        Bot bot = new Bot(board, Constants.O_ON_BOARD);
        Game game = new Game(board, bot);

        GameResponse response = mapper.toGameResponse(game);

        assertAll("Empty Game Mapping",
                () -> assertThat(response).as("Response should not be null").isNotNull(),
                () -> assertThat(response.getStatus()).as("Game status should be PLAYER_TURN")
                        .isEqualTo(GameStatus.PLAYER_TURN),
                () -> assertThat(response.getWinner()).as("Winner should be null").isNull(),
                () -> assertThat(response.getBoard()).as("Board should have correct rows").hasSize(Constants.ROWS),
                () -> assertThat(response.getBoard().get(0)).as("Board col 0 should have correct cols")
                        .hasSize(Constants.COLS),
                () -> assertThat(response.getBoard().get(0).get(0)).as("First cell should be empty (_0)")
                        .isEqualTo(BoardCellEnum._0));
    }

    @Test
    @DisplayName("Should map board values correctly (X -> X, Y -> O)")
    void shouldMapBoardValues() {
        Board board = new Board(Constants.ROWS, Constants.COLS);
        Bot bot = new Bot(board, Constants.O_ON_BOARD);
        Game game = new Game(board, bot);

        // Simulating a move
        board.setMove(0); // Player move (X)
        board.setMove(1); // Bot move (O/Y)

        GameResponse response = mapper.toGameResponse(game);
        List<List<BoardCellEnum>> mappedBoard = response.getBoard();

        List<BoardCellEnum> bottomRow = mappedBoard.get(Constants.ROWS - 1);

        assertAll("Board Values Mapping",
                () -> assertThat(bottomRow.get(0)).as("First cell in bottom row should be X")
                        .isEqualTo(BoardCellEnum.X),
                () -> assertThat(bottomRow.get(1)).as("Second cell in bottom row should be Y (Bot)")
                        .isEqualTo(BoardCellEnum.Y));
    }
}
