package ConnectFour;

public class Main {
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='O';
	final static int cols=7;
	final static int rows=6;
	
	/**
	 * @param board the board of the game
	 */
	public static void reset(char[][] board){
		for (int i=0 ; i<board.length-3 ; i++){
			for (int j=0 ; j<board[i].length ; j++){
				board[i][j] = nullOnBoard;
			}
		}
	}
	
	public static char[] getCol(char[][] board, int index){
		char[] col = new char[board.length];
		for (int i=0 ; i<board.length ; i++)
			col[i] = board[i][index];
		return col;
	}
	
	/**
	 * @param board the board of the game
	 * @return 1-X wined 2-O wined 0-no winner in a specific row
	 */
	public static int rowWinner(char[] array){
		for (int i=0 ; i<array.length-3 ; i++){
			if (array[i]==array[i+1]&&array[i+2]==array[i+3]&&array[i]==array[i+2]){
				if (array[i]==xOnBoard)
					return 1;
				else if (array[i]==oOnBoard)
					return 2;
			}
		}
		return 0;
	}
	
	public static int colWinner(char[] array){
		for (int i=0 ; i<array.length-3 ; i++){
			if (array[i]==array[i+1]&&array[i+2]==array[i+3]&&array[i]==array[i+2]){
				if (array[i]==xOnBoard)
					return 1;
				else if (array[i]==oOnBoard)
					return 2;
			}
		}
		return 0;
	}

	public static int Winner(char[][] board){
	
		return 0;
	}

	public static int rowWnner(char[][] board){
		
		return 0;
	}
	
	/**
	 * @param board the board of the game
	 * @return 1-X wined 2-O wined 0-no winner
	 */
	public static int winner(char[][] board){
		int winner;
		
		for (int i=0 ; i<rows ; i++){
			winner = rowWinner(board[i]);
			if (winner!=0)
				return winner;
		}
		
		for (int i=0 ; i<cols ; i++){
			winner = colWinner(getCol(board,i));
			if (winner!=0)
				return winner;
		}
		return 0;
	}

	public static void main(String[] args) {

	}
}
