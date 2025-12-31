package org.omer.connectfour.service;

import org.omer.connectfour.api.model.BoardCellEnum;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.GameStatus;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.api.model.Position;
import org.omer.connectfour.api.model.WinnerEnum;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.utils.Constants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.omer.connectfour.utils.Constants.*;

/**
 * Service responsible for mapping internal Domain Models to API DTOs.
 */
@Component
public class GameMapper {

    /**
     * Maps the internal Game entity to a GameResponse DTO.
     *
     * @param game The game entity.
     * @return The response DTO containing board state and status.
     */
    public GameResponse toGameResponse(Game game) {
        GameResponse response = new GameResponse();
        response.setId(game.getUuid());
        response.setBoard(mapBoard(game.getBoard().getBoard()));

        int winner = game.getBoard().winner();
        if (winner == X_WON) {
            response.setStatus(GameStatus.PLAYER_WON);
            response.setWinner(WinnerEnum.PLAYER);
        } else if (winner == O_WON) {
            response.setStatus(GameStatus.BOT_WON);
            response.setWinner(WinnerEnum.BOT);
        } else if (game.getBoard().isFull()) {
            response.setStatus(GameStatus.DRAW);
            response.setWinner(null);
        } else {
            response.setStatus(GameStatus.PLAYER_TURN);
            response.setWinner(null);
        }

        return response;
    }

    /**
     * Creates a MoveResponse DTO detailing the result of a move.
     *
     * @param game       The game entity.
     * @param success    Whether the move was successful.
     * @param playerMove The position of the player's move.
     * @param botMove    The position of the bot's move (can be null).
     * @param status     The current game status.
     * @param winner     The winner (if any).
     * @return The comprehensive move response.
     */
    public MoveResponse createMoveResponse(Game game, boolean success, Position playerMove, Position botMove,
            GameStatus status, WinnerEnum winner) {
        MoveResponse response = new MoveResponse();
        response.setSuccess(success);
        response.setPlayerMove(playerMove);
        response.setBotMove(botMove);
        response.setBoard(mapBoard(game.getBoard().getBoard()));
        response.setStatus(status);
        response.setWinner(winner);
        return response;
    }

    /**
     * Maps the internal char[][] board to a strongly-typed List representation.
     *
     * @param boardArray The internal board array.
     * @return A list of lists containing BoardCellEnum values.
     */
    private List<List<BoardCellEnum>> mapBoard(char[][] boardArray) {
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
}
