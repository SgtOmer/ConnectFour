package ConnectFour;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class mainTests {
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='O';
	Board board;

	@Test
	public void testReset() {
		System.out.println("reset checking");
		Board board=new Board(6,7);
		board.reset();
		char[][] Board=board.board;
		for (int i=0 ; i<Board.length ; i++){
			for (int j=0 ; j<Board[i].length ; j++){
				if (Board[i][j]!=nullOnBoard)
					fail("on row: "+i+" and column: "+j);
			}
		}
		for (int i=0 ; i<Board.length ; i++){
			for (int j=0 ; j<Board[i].length ; j++){
				System.out.print(Board[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	@Before
	public void setUp(){
		System.out.println("setting up");
		board=new Board(4,5);
		char[][] array = new char[][]{
			{'1','2','3','4','5'},
			{'2','3','4','5','6'},
			{'3','4','5','6','7'},
			{'4','5','6','7','8'}
		};
		board.board=array;
	}
	
	@Test
	public void gettingCol(){
		System.out.println("col checking");
		int colNum=(int)(Math.random()*4);
		System.out.println("col number- "+colNum);
		char[] col = board.getCol(colNum);
		for (int i=0 ; i<col.length ; i++){
			System.out.print(col[i]+", ");
		}
		System.out.println();
		for (int i=0 ; i<board.board.length ; i++){
			if (col[i] != board.board[i][colNum])
				fail();
		}
	}
	
	@Test
	public void gettingMainDiagnol(){
		System.out.println("main diagnol checking");
		int colNum=(int)(Math.random()*4);
		int rowNum=(int)(Math.random()*3);
		System.out.println("col number- "+colNum+" row number- "+rowNum);
		char[] dia = board.getMainDiagnol(rowNum, colNum);
		for (int i=0 ; i<dia.length ; i++){
			System.out.print(dia[i]+", ");
		}
		System.out.println();
		for (int i=0 ; i<board.board.length-rowNum && i<board.board[0].length-colNum ; i++){
			if (dia[i]!=board.board[rowNum+i][colNum+i])
				fail();
		}
	}
	
	@Test
	public void gettingSecDiagnol(){
		System.out.println("Sec diagnol checking");
		int colNum=(int)(Math.random()*4);
		int rowNum=(int)(Math.random()*3);
		System.out.println("col number- "+colNum+" row number- "+rowNum);
		char[] dia = board.getSecDiagnol(rowNum, colNum);
		for (int i=0 ; i<dia.length ; i++){
			System.out.print(dia[i]+", ");
		}
		System.out.println();
		for (int i=0 ; i<board.board.length-rowNum && i<colNum ; i++){
			if (dia[i]!=board.board[rowNum+i][colNum-i])
				fail();
		}
	}
	
	@Test
	public void arrayWinnerTest(){
		System.out.println("array winner checking");
		char[] xWins = new char[]{'0','O','X','X','X','X','O'};
		char[] yWins = new char[]{'0','0','O','O','O','O','O'};
		char[] noWinner = new char[]{'0','0','X','O','0','X','0'};
		assertEquals(1, arrayWinner(xWins));
		assertEquals(2, arrayWinner(yWins));
		assertEquals(0, arrayWinner(noWinner));
	}
	
	public static int arrayWinner(char[] array){
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
	
	@Test
	public void winnerTest(){
		System.out.println("winner checking");
		char[][] xWins = new char[][]{
			{'X','0','0','X','X'},
			{'0','X','0','O','O'},
			{'0','0','X','0','X'},
			{'X','0','0','X','0'}
		};
		board.board=xWins;
		assertEquals(1, board.winner());
		
		char[][] yWins = new char[][]{
			{'0','0','O','O','O'},
			{'0','0','O','X','0'},
			{'0','O','X','O','O'},
			{'O','0','O','O','O'}
		};
		board.board=yWins;
		assertEquals(2, board.winner());
		
		char[][] noWinner = new char[][]{
			{'0','0','X','O','0'},
			{'0','0','O','O','O'},
			{'0','0','O','0','0'},
			{'0','0','0','O','O'},
		};
		board.board=noWinner;
		assertEquals(0, board.winner());
		
		Board newB= new Board(6,7);
		char[][] no = new char[][]{
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','O','0','0','0'},
			{'0','0','0','X','0','0','0'},
			{'0','0','0','X','0','0','0'},
			{'0','O','O','X','0','O','O'},
		};
		newB.board=no;
		assertEquals(0, newB.winner());
	}
	
	@Test
	public void lowestPlace(){
		System.out.println("lowest in col checking");
		char[][] map = new char[][]{
			{'0','0','0','O','0'},
			{'0','X','0','O','0'},
			{'0','O','X','O','0'},
			{'X','X','O','X','0'}
		};
		board.board=map;
		assertEquals(2, board.getLowest(0));
		assertEquals(0, board.getLowest(1));
		assertEquals(1, board.getLowest(2));
		assertEquals(-1,board.getLowest(3));
		assertEquals(3, board.getLowest(4));
	}
	
	@Test
	public void isLegalCheck(){
		System.out.println("is Legal checking");
		char[][] map = new char[][]{
			{'0','0','0','O','0'},
			{'0','X','0','O','0'},
			{'0','O','X','O','0'},
			{'X','X','O','X','0'}
		};
		board.board=map;
		assertEquals(false, board.isLegal(5));
		assertEquals(false, board.isLegal(-1));
		assertEquals(false, board.isLegal(3));
		assertEquals(true, board.isLegal(0));
		assertEquals(true, board.isLegal(1));
		assertEquals(true, board.isLegal(2));
		assertEquals(true, board.isLegal(4));
	}
	
	@Test
	public void isFullCheck(){
		System.out.println("is Full checking");
		char[][] full = new char[][]{
			{'X','O','O','X','X'},
			{'O','X','O','O','O'},
			{'O','O','X','O','X'},
			{'X','O','O','X','O'}
		};
		board.board=full;
		assertEquals(true, board.isFull());
		
		char[][] notFull = new char[][]{
			{'X','O','0','X','X'},
			{'O','X','O','O','O'},
			{'O','O','X','O','X'},
			{'X','O','O','X','O'}
		};
		board.board=notFull;
		assertEquals(false, board.isFull());
	}
	
	@Test
	public void setMoveCheck(){
		System.out.println("set move checking");
		char[][] full = new char[][]{
			{'0','0','0','0','0'},
			{'O','X','O','O','O'},
			{'O','O','X','O','X'},
			{'X','O','O','X','O'}
		};
		board.board=full;
		board.setMove(0);
		assertNotEquals(nullOnBoard, board.board[0][0]);
	}
	
	@Test
	public void delMoveCheck(){
		System.out.println("delete move checking");
		char[][] full = new char[][]{
			{'O','0','0','0','0'},
			{'O','X','O','O','O'},
			{'O','O','X','O','X'},
			{'X','O','O','X','O'}
		};
		board.board=full;
		board.delMove(0);
		assertEquals(nullOnBoard, board.board[0][0]);
	}
	
	@Test
	public void calculateLineCheck(){
		System.out.println("caclculate line checking");
		Bot bot = new Bot(board,xOnBoard);
		char[] oneArea = new char[]{'0','0','0','X','0','0','0'};
		assertEquals(4, bot.calculateLine(oneArea));
		char[] twoArea = new char[]{'0','0','X','X','0','0','0'};
		assertEquals(31, bot.calculateLine(twoArea));
		char[] threeArea = new char[]{'0','0','X','X','X','0','0'};
		assertEquals(220, bot.calculateLine(threeArea));
		char[] fourArea = new char[]{'0','X','X','X','X','0','0'};
		assertEquals(10210, bot.calculateLine(fourArea));
	}
	
	@Test
	public void calculateCheck(){
		System.out.println("caclculate checking");
		char[][] oneArea = new char[][]{
				{'0','0','0','0','0','0','0'},
				{'0','0','0','0','0','0','0'},
				{'0','0','0','0','0','0','0'},
				{'0','0','0','0','0','0','0'},
				{'0','0','0','0','0','0','0'},
				{'0','0','0','X','0','0','0'},
		};
		Board board = new Board(6,7);
		board.board = oneArea;
		Bot bot = new Bot(board,xOnBoard);
		assertEquals(7, bot.calculate());
		char[][] twoArea = new char[][]{
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','X','X','0','0'},
		};
		board.board = twoArea;
		bot = new Bot(board,xOnBoard);
		assertEquals(36, bot.calculate());
		char[][] threeArea = new char[][]{
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','X','X','0','0'},
			{'0','0','0','X','X','0','0'},
		};
		board.board = threeArea;
		bot = new Bot(board,xOnBoard);
		assertEquals(110, bot.calculate());
		char[][] fourArea = new char[][]{
			{'0','0','0','0','0','0','0'},
			{'0','0','0','0','0','0','0'},
			{'0','0','0','O','0','0','0'},
			{'0','0','0','X','0','0','0'},
			{'0','0','0','X','0','0','0'},
			{'0','0','O','X','0','O','0'},
		};
		board.board = fourArea;
		bot = new Bot(board,oOnBoard);
		assertEquals(-6, bot.calculate());
	}
}
