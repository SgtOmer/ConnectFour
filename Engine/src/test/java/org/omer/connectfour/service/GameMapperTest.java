package org.omer.connectfour.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.omer.connectfour.api.model.BoardCellEnum;
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
    @DisplayName("Should map empty board correctly")
    void shouldMapEmptyBoard() {
        Board board = new Board(Constants.ROWS, Constants.COLS);

        List<List<BoardCellEnum>> mappedBoard = mapper.mapBoard(board.getBoard());

        assertAll("Empty Board Mapping",
                () -> assertThat(mappedBoard).as("Board should have correct rows")
                        .hasSize(Constants.ROWS),
                () -> assertThat(mappedBoard.get(0)).as("First row should have correct cols")
                        .hasSize(Constants.COLS),
                () -> assertThat(mappedBoard.get(0).get(0)).as("First cell should be empty")
                        .isEqualTo(BoardCellEnum._0));
    }

    @Test
    @DisplayName("Should map board values correctly (X -> X, O -> Y)")
    void shouldMapBoardValues() {
        Board board = new Board(Constants.ROWS, Constants.COLS);
        board.setMove(0); // Player move (X)
        board.setMove(1); // Bot move (O/Y)

        List<List<BoardCellEnum>> mappedBoard = mapper.mapBoard(board.getBoard());
        List<BoardCellEnum> bottomRow = mappedBoard.get(Constants.ROWS - 1);

        assertAll("Board Values Mapping",
                () -> assertThat(bottomRow.get(0)).as("First cell should be X")
                        .isEqualTo(BoardCellEnum.X),
                () -> assertThat(bottomRow.get(1)).as("Second cell should be Y")
                        .isEqualTo(BoardCellEnum.Y));
    }

    @Test
    @DisplayName("Should determine ONGOING status for new game")
    void shouldDetermineOngoingStatus() {
        Board board = new Board(Constants.ROWS, Constants.COLS);
        Bot bot = new Bot(board, Constants.O_ON_BOARD);
        Game game = new Game(board, bot);

        GameStatus status = mapper.determineGameStatus(game);

        assertThat(status).isEqualTo(GameStatus.ONGOING);
    }
}
