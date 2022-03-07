package org.omer.connectfour.bot;

import org.omer.connectfour.model.Board;
import org.omer.connectfour.enums.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.omer.connectfour.utils.CONSTANTS.NULL_ON_BOARD;
import static org.omer.connectfour.utils.CONSTANTS.X_ON_BOARD;
import static org.omer.connectfour.utils.CONSTANTS.O_ON_BOARD;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    private static final int[] MOVE_ORDER_CHECK = {3, 2, 4, 1, 5, 0, 6};

    private final Board board;
    private final char sign;
    private final char enemy;

    public Bot(Board board, char sign) {
        this.board = board;
        this.sign = sign;
        if (sign == X_ON_BOARD) {
            enemy = O_ON_BOARD;
        } else
            enemy = X_ON_BOARD;
    }

    public int[] makeMove(int turn, Player player, int alpha, int beta) {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE * player.getValue();
        int score;
        if (turn == 10 || board.winner() != 0 || board.isFull())
            return new int[]{calculate(), bestMove};
        for (int i: MOVE_ORDER_CHECK) {
            if (!board.isLegal(i))
                continue;

            board.setMove(i);
            score = makeMove(turn + 1, Player.getOther(player), alpha, beta)[0];
            board.delMove(i);

            if (turn == 0)
                LOGGER.debug("move: " + (i + 1) + " score: " + score);

            if (score * player.getValue() > 8500)
                return new int[]{score, i};

            if (score * player.getValue() > bestScore) {
                bestScore = score;
                bestMove = i;
            }

            if (player == Player.BOT)
                alpha = Math.max(bestScore, alpha);
            else
                beta = Math.min(bestScore, beta);

            if (alpha >= beta)
                break;
        }
        return new int[]{bestScore, bestMove};
    }

    public int makeMove(int turn, Player player) {
        return makeMove(turn, player, Integer.MIN_VALUE, Integer.MAX_VALUE)[1];
    }

    public int calculate() {
        return board.getAllArrays().stream()
                .mapToInt(this::calculateLine)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public int calculateLine(char[] line) {
        int score = 0;

        for (int i = 0; i < line.length - 3; i++) {
            //four area
            if (line[i] == line[i + 1] && line[i + 2] == line[i + 3] && line[i] == line[i + 2]) {
                if (line[i] == sign)
                    score += 10000;
                else if (line[i] == enemy)
                    score -= 10000;
            }
            //three area
            if (line[i] == line[i + 1] && line[i + 2] == line[i] && NULL_ON_BOARD == line[i + 3]) {
                if (line[i] == sign)
                    score += 100;
                else if (line[i] == enemy)
                    score -= 100;
            }
            if (line[i] == line[i + 1] && line[i] == line[i + 3] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i] == sign)
                    score += 100;
                else if (line[i] == enemy)
                    score -= 100;
            }
            if (line[i] == line[i + 2] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i + 1]) {
                if (line[i] == sign)
                    score += 100;
                else if (line[i] == enemy)
                    score -= 100;
            }
            if (line[i + 2] == line[i + 1] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i]) {
                if (line[i + 1] == sign)
                    score += 100;
                else if (line[i + 1] == enemy)
                    score -= 100;
            }
            //two area
            if (line[i] == line[i + 1] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i] == sign)
                    score += 10;
                else if (line[i] == enemy)
                    score -= 10;
            }
            if (line[i] == line[i + 3] && line[i + 2] == line[i + 1] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i] == sign)
                    score += 10;
                else if (line[i] == enemy)
                    score -= 10;
            }
            if (line[i] == line[i + 1] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i]) {
                if (line[i + 2] == sign)
                    score += 10;
                else if (line[i + 2] == enemy)
                    score -= 10;
            }
            if (line[i + 3] == line[i + 1] && line[i + 2] == line[i] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i + 1] == sign)
                    score += 10;
                else if (line[i + 1] == enemy)
                    score -= 10;
            }
            if (line[i + 2] == line[i + 1] && line[i] == line[i + 3] && NULL_ON_BOARD == line[i]) {
                if (line[i + 1] == sign)
                    score += 10;
                else if (line[i + 1] == enemy)
                    score -= 10;
            }
            if (line[i] == line[i + 2] && line[i + 1] == line[i + 3] && NULL_ON_BOARD == line[i + 1]) {
                if (line[i] == sign)
                    score += 10;
                else if (line[i] == enemy)
                    score -= 10;
            }
            //one area
            if (line[i + 2] == line[i + 1] && line[i + 2] == line[i + 3] && line[i + 1] == NULL_ON_BOARD) {
                if (line[i] == sign)
                    score += 1;
                else if (line[i] == enemy)
                    score -= 1;
            }
            if (line[i] == line[i + 2] && line[i + 2] == line[i + 3] && line[i] == NULL_ON_BOARD) {
                if (line[i + 1] == sign)
                    score += 1;
                else if (line[i + 1] == enemy)
                    score -= 1;
            }
            if (line[i] == line[i + 1] && line[i + 1] == line[i + 3] && line[i] == NULL_ON_BOARD) {
                if (line[i + 2] == sign)
                    score += 1;
                else if (line[i + 2] == enemy)
                    score -= 1;
            }
            if (line[i] == line[i + 1] && line[i + 2] == line[i] && line[i] == NULL_ON_BOARD) {
                if (line[i + 3] == sign)
                    score += 1;
                else if (line[i + 3] == enemy)
                    score -= 1;
            }
        }

        return score;
    }
}
