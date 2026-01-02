package org.omer.connectfour.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.omer.connectfour.utils.Constants.NO_WINNER;
import static org.omer.connectfour.utils.Constants.NULL_ON_BOARD;
import static org.omer.connectfour.utils.Constants.O_WON;
import static org.omer.connectfour.utils.Constants.X_ON_BOARD;
import static org.omer.connectfour.utils.Constants.O_ON_BOARD;
import static org.omer.connectfour.utils.Constants.X_WON;

/**
 * Represents the Connect Four game board with logic for moves, winning
 * detection, and board state.
 */
@Getter
@Setter
public class Board {
    private char[][] board;
    private int cols;
    private int rows;
    private int turn;

    public Board(int rows, int cols) {
        board = new char[rows][cols];
        this.cols = cols;
        this.rows = rows;
        reset();
    }

    public Board(Board board) {
        cols = board.board[0].length;
        rows = board.board.length;
        turn = board.turn;
        this.board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(board.board[i], 0, this.board[i], 0, cols);
        }
    }

    public void reset() {
        for (char[] chars : board) {
            Arrays.fill(chars, NULL_ON_BOARD);
        }
        turn = 0;
    }

    public int winner() {
        List<char[]> arrays = getAllArrays();

        return arrays.stream()
                .mapToInt(Board::arrayWinner)
                .filter((int winner) -> winner != NO_WINNER)
                .findFirst()
                .orElse(NO_WINNER);
    }

    public boolean isLegal(int move) {
        if (move > cols - 1 || move < 0)
            return false;

        return getLowest(move) != -1;
    }

    public boolean setMove(int move) {
        if (!isLegal(move))
            return false;

        int lowest = getLowest(move);
        if (turn++ % 2 == 0)
            board[lowest][move] = X_ON_BOARD;
        else
            board[lowest][move] = O_ON_BOARD;

        return true;
    }

    public boolean isFull() {
        for (int i = 0; i < cols; i++) {
            if (isLegal(i))
                return false;
        }
        return true;
    }

    public void delMove(int move) {
        int lowest = getLowest(move);
        board[lowest + 1][move] = NULL_ON_BOARD;
        turn--;
    }

    public void show() {
        for (char[] chars : board) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    public List<char[]> getAllArrays() {
        List<char[]> arrays = new ArrayList<>();

        for (int i = 0; i < rows; i++)
            arrays.add(board[i]);

        for (int i = 0; i < cols; i++)
            arrays.add(getCol(i));

        for (int i = 0; i < cols - 3; i++)
            arrays.add(getMainDiagonal(0, i));

        for (int i = 1; i < rows - 3; i++)
            arrays.add(getMainDiagonal(i, 0));

        for (int i = cols - 1; i > 2; i--)
            arrays.add(getSecDiagonal(0, i));

        for (int i = 1; i < rows - 3; i++)
            arrays.add(getSecDiagonal(i, cols - 1));

        return arrays;
    }

    public char[] getCol(int index) {
        char[] col = new char[board.length];
        for (int i = 0; i < board.length; i++)
            col[i] = board[i][index];
        return col;
    }

    public char[] getMainDiagonal(int row, int col) {
        char[] diagonal = new char[Math.min(board.length - row, board[0].length - col)];
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = board[row + i][col + i];
        return diagonal;
    }

    public char[] getSecDiagonal(int row, int col) {
        char[] diagonal = new char[Math.min(board.length - row, col + 1)];
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = board[row + i][col - i];
        return diagonal;
    }

    public static int arrayWinner(char[] array) {
        for (int i = 0; i < array.length - 3; i++) {
            if (array[i] == array[i + 1] && array[i + 2] == array[i + 3] && array[i] == array[i + 2]) {
                if (array[i] == X_ON_BOARD)
                    return X_WON;
                else if (array[i] == O_ON_BOARD)
                    return O_WON;
            }
        }
        return NO_WINNER;
    }

    public int getLowest(int col) {
        return IntStream.range(0, rows)
                .filter((int row) -> board[row][col] == NULL_ON_BOARD)
                .max()
                .orElse(-1);
    }

}
