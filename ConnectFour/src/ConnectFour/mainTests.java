package ConnectFour;

import static org.junit.Assert.*;
import org.junit.Test;

public class mainTests {
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='O';

	@Test
	public void testReset() {
		char[][] board = new char[6][7];
		reset(board);
		for (int i=0 ; i<board.length ; i++){
			for (int j=0 ; j<board[i].length ; j++){
				if (board[i][j]!=nullOnBoard)
					fail("on row: "+i+" and column: "+j);
			}
		}
	}

	private static void reset(char[][] board){
		for (int i=0 ; i<board.length ; i++){
			for (int j=0 ; j<board[i].length ; j++){
				board[i][j] = nullOnBoard;
			}
		}
	}
}
