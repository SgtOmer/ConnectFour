package org.omer.connectfour.service;

import org.omer.connectfour.api.model.BoardCellEnum;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.utils.Constants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for mapping internal board to API format.
 */
@Component
public class GameMapper {
    /**
     * Maps the internal char[][] board to a strongly-typed List representation.
     *
     * @param boardArray The internal board array.
     * @return A list of lists containing BoardCellEnum values.
     */
    public List<List<BoardCellEnum>> mapBoard(char[][] boardArray) {
        List<List<BoardCellEnum>> boardList = new ArrayList<>();
        for (char[] row : boardArray) {
            List<BoardCellEnum> rowList = new ArrayList<>();
            for (char cell : row) {
                if (cell == Constants.X_ON_BOARD) {
                    rowList.add(BoardCellEnum.X);
                } else if (cell == Constants.O_ON_BOARD) {
                    rowList.add(BoardCellEnum.Y);
                } else {
                    rowList.add(BoardCellEnum._0);
                }
            }
            boardList.add(rowList);
        }

        return boardList;
    }

    /**
     * Determines the game status from the game state.
     *
     * @param game The game entity.
     * @return The corresponding GameStatus.
     */
    public org.omer.connectfour.api.model.GameStatus determineGameStatus(Game game) {
        int winner = game.getBoard().winner();
        if (winner == Constants.X_WON) {
            return org.omer.connectfour.api.model.GameStatus.PLAYER_WON;
        }

        if (winner == Constants.O_WON) {
            return org.omer.connectfour.api.model.GameStatus.BOT_WON;
        }

        if (game.getBoard().isFull()) {
            return org.omer.connectfour.api.model.GameStatus.DRAW;
        }

        return org.omer.connectfour.api.model.GameStatus.ONGOING;
    }
}
