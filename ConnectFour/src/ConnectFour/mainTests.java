package ConnectFour;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class mainTests {
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='O';
	char[][] array;

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
	
	@Before
	public void setUp(){
		array = new char[][]{
			{'1','2','3','4','5'},
			{'2','3','4','5','6'},
			{'3','4','5','6','7'},
			{'4','5','6','7','8'}
		};
	}
	
	@Test
	public void gettingCol(){
		int colNum=(int)(Math.random()*5);
		char[] col = getCol(array,colNum);
		for (int i=0 ; i<array.length ; i++){
			if (col[i] != array[i][colNum])
				fail();
		}
	}
	
	public static char[] getCol(char[][] board, int index){
		char[] col = new char[board.length];
		for (int i=0 ; i<board.length ; i++)
			col[i] = board[i][index];
		return col;
	}
}
