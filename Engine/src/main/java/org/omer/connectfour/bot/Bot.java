package org.omer.connectfour.bot;

import lombok.extern.log4j.Log4j2;
import org.omer.connectfour.model.Board;
import org.omer.connectfour.enums.Player;

import static org.omer.connectfour.utils.Constants.NULL_ON_BOARD;
import static org.omer.connectfour.utils.Constants.X_ON_BOARD;
import static org.omer.connectfour.utils.Constants.O_ON_BOARD;

/**
 * AI bot for Connect Four using minimax algorithm with alpha-beta pruning.
 * Evaluates board positions and determines optimal moves.
 */
@Log4j2
public class Bot {
    private static final int[] MOVE_ORDER_CHECK = { 3, 2, 4, 1, 5, 0, 6 };

    private final Board board;
    private final char sign;
    private final char enemy;

    /**
     * Constructs a new Bot with the specified board and piece sign.
     *
     * @param board The game board.
     * @param sign  The character representing this bot's pieces.
     */
    public Bot(Board board, char sign) {
        this.board = board;
        this.sign = sign;
        if (sign == X_ON_BOARD) {
            enemy = O_ON_BOARD;
        } else {
            enemy = X_ON_BOARD;
        }
    }

    /**
     * Executes minimax with alpha-beta pruning to find the best move.
     *
     * @param turn   Current turn depth.
     * @param player Current player.
     * @param alpha  Alpha value for pruning.
     * @param beta   Beta value for pruning.
     * @return Array containing [score, bestMove].
     */
    public int[] makeMove(int turn, Player player, int alpha, int beta) {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE * player.getValue();
        int score;

        if (turn == 10 || board.winner() != 0 || board.isFull()) {
            return new int[] { calculate(), bestMove };
        }

        for (int i : MOVE_ORDER_CHECK) {
            if (!board.isLegal(i)) {
                continue;
            }

            board.setMove(i);
            score = makeMove(turn + 1, Player.getOther(player), alpha, beta)[0];
            board.delMove(i);

            if (turn == 0) {
                log.debug("move: {} score: {}", i + 1, score);
            }

            if (score * player.getValue() > 8500) {
                return new int[] { score, i };
            }

            if (score * player.getValue() > bestScore) {
                bestScore = score;
                bestMove = i;
            }

            if (player == Player.BOT) {
                alpha = Math.max(bestScore, alpha);
            } else {
                beta = Math.min(bestScore, beta);
            }

            if (alpha >= beta) {
                break;
            }
        }

        return new int[] { bestScore, bestMove };
    }

    /**
     * Convenience method to get the best column to play.
     *
     * @param turn   Current turn depth.
     * @param player Current player.
     * @return The column index of the best move.
     */
    public int makeMove(int turn, Player player) {
        return makeMove(turn, player, Integer.MIN_VALUE, Integer.MAX_VALUE)[1];
    }

    /**
     * Calculates the overall board score from the bot's perspective.
     *
     * @return The calculated score.
     */
    public int calculate() {
        return board.getAllArrays().stream()
                .mapToInt(this::calculateLine)
                .reduce(Integer::sum)
                .orElse(0);
    }

    /**
     * Evaluates a single line (row, column, or diagonal) for scoring.
     *
     * @param line The character array representing the line.
     * @return The score for this line.
     */
    public int calculateLine(char[] line) {
        int score = 0;

        for (int i = 0; i < line.length - 3; i++) {
            // four area
            if (line[i] == line[i + 1] && line[i + 2] == line[i + 3] && line[i] == line[i + 2]) {
                if (line[i] == sign) {
                    score += 10000;
                } else if (line[i] == enemy) {
                    score -= 10000;
                }
            }

            // three area
            if (line[i] == line[i + 1] && line[i + 2] == line[i] && NULL_ON_BOARD == line[i + 3]) {
                if (line[i] == sign) {
                    score += 100;
                } else if (line[i] == enemy) {
                    score -= 100;
                }
            }

            if (line[i] == line[i + 1] && line[i] == line[i + 3] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i] == sign) {
                    score += 100;
                } else if (line[i] == enemy) {
                    score -= 100;
                }
            }

            if (line[i] == line[i + 2] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i + 1]) {
                if (line[i] == sign) {
                    score += 100;
                } else if (line[i] == enemy) {
                    score -= 100;
                }
            }

            if (line[i + 2] == line[i + 1] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i]) {
                if (line[i + 1] == sign) {
                    score += 100;
                } else if (line[i + 1] == enemy) {
                    score -= 100;
                }
            }

            // two area
            if (line[i] == line[i + 1] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i] == sign) {
                    score += 10;
                } else if (line[i] == enemy) {
                    score -= 10;
                }
            }

            if (line[i] == line[i + 3] && line[i + 2] == line[i + 1] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i] == sign) {
                    score += 10;
                } else if (line[i] == enemy) {
                    score -= 10;
                }
            }

            if (line[i] == line[i + 1] && line[i + 2] == line[i + 3] && NULL_ON_BOARD == line[i]) {
                if (line[i + 2] == sign) {
                    score += 10;
                } else if (line[i + 2] == enemy) {
                    score -= 10;
                }
            }

            if (line[i + 3] == line[i + 1] && line[i + 2] == line[i] && NULL_ON_BOARD == line[i + 2]) {
                if (line[i + 1] == sign) {
                    score += 10;
                } else if (line[i + 1] == enemy) {
                    score -= 10;
                }
            }

            if (line[i + 2] == line[i + 1] && line[i] == line[i + 3] && NULL_ON_BOARD == line[i]) {
                if (line[i + 1] == sign) {
                    score += 10;
                } else if (line[i + 1] == enemy) {
                    score -= 10;
                }
            }

            if (line[i] == line[i + 2] && line[i + 1] == line[i + 3] && NULL_ON_BOARD == line[i + 1]) {
                if (line[i] == sign) {
                    score += 10;
                } else if (line[i] == enemy) {
                    score -= 10;
                }
            }

            // one area
            if (line[i + 2] == line[i + 1] && line[i + 2] == line[i + 3] && line[i + 1] == NULL_ON_BOARD) {
                if (line[i] == sign) {
                    score += 1;
                } else if (line[i] == enemy) {
                    score -= 1;
                }
            }

            if (line[i] == line[i + 2] && line[i + 2] == line[i + 3] && line[i] == NULL_ON_BOARD) {
                if (line[i + 1] == sign) {
                    score += 1;
                } else if (line[i + 1] == enemy) {
                    score -= 1;
                }
            }

            if (line[i] == line[i + 1] && line[i + 1] == line[i + 3] && line[i] == NULL_ON_BOARD) {
                if (line[i + 2] == sign) {
                    score += 1;
                } else if (line[i + 2] == enemy) {
                    score -= 1;
                }
            }

            if (line[i] == line[i + 1] && line[i + 2] == line[i] && line[i] == NULL_ON_BOARD) {
                if (line[i + 3] == sign) {
                    score += 1;
                } else if (line[i + 3] == enemy) {
                    score -= 1;
                }
            }
        }

        return score;
    }
}
