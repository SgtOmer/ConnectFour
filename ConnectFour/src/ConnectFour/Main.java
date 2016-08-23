package ConnectFour;

import java.util.Scanner;

public class Main {
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='O';
	final static int cols=7;
	final static int rows=6;
	
	
	static Scanner reader = new Scanner(System.in);
	public static void main(String[] args) {
		Board board = new Board(6,7);
		int turn=0;
		int winner=0;
		int move;
		int again=1;
		boolean human=false;
		do{
			board.reset();
			board.show();
			turn=0;
			winner=0;
			if (human){
				do {
					System.out.println("enter your column number to play");
					move=reader.nextInt()-1;
					if (!board.setMove(move)){
						System.out.println("your move was illeagal, please try again");
						continue;
					}
					board.show();
					winner=board.winner();
					turn++;
				} while(turn<42&&winner==0);
			} else {
				Bot bot;
				do {
					if (turn%2==0){
						System.out.println("enter your column number to play");
						move=reader.nextInt()-1;
						if (!board.setMove(move)){
							System.out.println("your move was illeagal, please try again");
							continue;
						}
					} else {
						bot = new Bot(board,oOnBoard);
						move = bot.makeMove(0, true)[1];
						board.setMove(move);
						System.out.println(move+1);
					}
					board.show();
					System.out.println();
					winner=board.winner();
					turn++;
				} while(turn<42&&winner==0);
			}
			if (winner!=0){
				if (winner==1)
					System.out.println("the first player won");
				else
					System.out.println("the second player won");
			} else
				System.out.println("there was a tie");
			System.out.println("would you like to play again? 1-yes 2-no");
			again=reader.nextInt();
		} while(again==1);
	}
}
