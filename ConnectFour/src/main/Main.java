package main;

import java.util.Scanner;

import static main.Utils.O_ON_BOARD;

public class Main {
    private static final Scanner reader = new Scanner(System.in);
    private static final Board board = new Board(Utils.ROWS, Utils.COLS);

    public static void main(String[] args) {
        int turn;
        int winner;
        int move;
        int again;
        boolean human = false;
        do {
            board.reset();
            board.show();
            turn = 0;
            winner = 0;
            if (human) {
                do {
                    System.out.println("enter your column number to play");
                    move = reader.nextInt() - 1;
                    if (!board.setMove(move)) {
                        System.out.println("your move was illeagal, please try again");
                        continue;
                    }
                    board.show();
                    winner = board.winner();
                    turn++;
                } while (turn < 42 && winner == 0);
            } else {
                Bot bot = new Bot(board, O_ON_BOARD);
                do {
                    if (turn % 2 == 0) {
                        System.out.println("enter your column number to play");
                        move = reader.nextInt() - 1;
                        if (!board.setMove(move)) {
                            System.out.println("your move was illeagal, please try again");
                            continue;
                        }
                    } else {
                        move = bot.makeMove(0, true)[1];
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
            again = reader.nextInt();
        } while (again == 1);
    }

    public static void announceWinner(int winner) {
        if (winner != 0) {
            if (winner == 1)
                System.out.println("the first player won");
            else
                System.out.println("the second player won");
        } else
            System.out.println("there was a tie");
    }
}
