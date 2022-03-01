package org.omer.connectfour;

import org.omer.connectfour.bot.Bot;
import org.omer.connectfour.enums.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

import static org.omer.connectfour.Utils.O_ON_BOARD;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /*private static final Scanner reader = new Scanner(System.in);
    private static final Board board = new Board(Utils.ROWS, Utils.COLS);

    public static void main(String[] args) {
        int turn;
        int winner;
        int move;
        boolean human = false;
        do {
            board.reset();
            board.show();
            turn = 0;
            winner = 0;
            if (human) {
                do {
                    if (!playerMove())
                        continue;

                    board.show();
                    System.out.println();
                    winner = board.winner();
                    turn++;
                } while (turn < 42 && winner == 0);
            } else {
                Bot bot = new Bot(board, O_ON_BOARD);
                do {
                    if (turn % 2 == 0) {
                        if (!playerMove())
                            continue;
                    } else {
                        move = bot.makeMove(0, Player.BOT);
                        board.setMove(move);
                        System.out.println(move + 1);
                    }
                    board.show();
                    System.out.println();
                    winner = board.winner();
                    turn++;
                } while (turn < 42 && winner == 0);
            }
            announceWinner(winner);
            System.out.println("would you like to play again? 1-yes 2-no");
        } while (reader.nextInt() == 1);
    }

    public static boolean playerMove() {
        System.out.println("enter your column number to play");
        int move = reader.nextInt() - 1;
        if (!board.setMove(move)) {
            System.out.println("your move was illegal, please try again");
            return false;
        }
        return true;
    }

    public static void announceWinner(int winner) {
        if (winner != 0) {
            if (winner == 1)
                System.out.println("the first player won");
            else
                System.out.println("the second player won");
        } else
            System.out.println("there was a tie");
    }*/
}
