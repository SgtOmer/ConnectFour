package main;

import static main.Utils.NULL_ON_BOARD;
import static main.Utils.X_ON_BOARD;
import static main.Utils.O_ON_BOARD;

public class Bot {
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

    public int[] makeMove(int turn, boolean bot, int alpha, int beta) {
        int bestMove = -1;
        int bestScore = (bot) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int score;
        if (turn == 10 || board.winner() != 0 || board.isFull()) {
            bestScore = calculate();
        } else {
            for (int i = 0; i < board.getBoard()[0].length; i++) {
                if (board.isLegal(i)) {
                    board.setMove(i);
                    score = makeMove(turn + 1, !bot, alpha, beta)[0];
                    if (turn == 0)
                        System.out.println("move: " + (i + 1) + " score: " + score);
                    if (bot) {
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = i;
                        }
                        alpha = Math.max(bestScore, alpha);
                        if (alpha >= beta) {
                            board.delMove(i);
                            break;
                        }
                    } else {
                        if (score < bestScore) {
                            bestScore = score;
                            bestMove = i;
                        }
                        beta = Math.min(bestScore, beta);
                        if (beta <= alpha) {
                            board.delMove(i);
                            break;
                        }
                    }
                    board.delMove(i);
                }
            }
        }
        return new int[]{bestScore, bestMove};
    }

    public int[] makeMove(int turn, boolean bot) {
        return makeMove(turn, bot, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
