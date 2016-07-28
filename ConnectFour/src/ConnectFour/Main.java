package ConnectFour;

public class Main {
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='O';
	
	/**
	 * @param board the board of the game
	 */
	public static void reset(char[][] board){
		for (int i=0 ; i<board.length ; i++){
			for (int j=0 ; j<board[i].length ; j++){
				board[i][j] = nullOnBoard;
			}
		}
	}

	public static void main(String[] args) {

	}
}
