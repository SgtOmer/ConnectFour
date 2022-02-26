package test;

import main.Board;
import main.Bot;
import org.junit.Before;
import org.junit.Test;

import static main.Board.arrayWinner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static test.TestUtils.NO_WINNER;
import static test.TestUtils.O_WON;
import static test.TestUtils.X_WON;
import static test.TestUtils.nullOnBoard;
import static test.TestUtils.xOnBoard;
import static test.TestUtils.oOnBoard;

public class mainTests {
    private Board board;

    @Test
    public void testReset() {
        System.out.println("reset checking");
        Board board = new Board(6, 7);
        board.reset();
        char[][] Board = board.getBoard();
        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                if (Board[i][j] != nullOnBoard)
                    fail("on row: " + i + " and column: " + j);
            }
        }
        for (char[] chars : Board) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    @Before
    public void setUp() {
        System.out.println("setting up");
        board = new Board(4, 5);
        char[][] array = new char[][]{
                {'1', '2', '3', '4', '5'},
                {'2', '3', '4', '5', '6'},
                {'3', '4', '5', '6', '7'},
                {'4', '5', '6', '7', '8'}
        };
        board.setBoard(array);
    }

    @Test
    public void gettingCol() {
        System.out.println("col checking");
        int colNum = (int) (Math.random() * 4);
        System.out.println("col number- " + colNum);
        char[] col = board.getCol(colNum);
        for (char c : col) {
            System.out.print(c + ", ");
        }
        System.out.println();
        for (int i = 0; i < board.getBoard().length; i++) {
            if (col[i] != board.getBoard()[i][colNum])
                fail();
        }
    }

    @Test
    public void gettingMainDiagnol() {
        System.out.println("main diagnol checking");
        int colNum = (int) (Math.random() * 4);
        int rowNum = (int) (Math.random() * 3);
        System.out.println("col number- " + colNum + " row number- " + rowNum);
        char[] dia = board.getMainDiagonal(rowNum, colNum);
        for (char c : dia) {
            System.out.print(c + ", ");
        }
        System.out.println();
        for (int i = 0; i < board.getBoard().length - rowNum && i < board.getBoard()[0].length - colNum; i++) {
            if (dia[i] != board.getBoard()[rowNum + i][colNum + i])
                fail();
        }
    }

    @Test
    public void gettingSecDiagnol() {
        System.out.println("Sec diagnol checking");
        int colNum = (int) (Math.random() * 4);
        int rowNum = (int) (Math.random() * 3);
        System.out.println("col number- " + colNum + " row number- " + rowNum);
        char[] dia = board.getSecDiagonal(rowNum, colNum);
        for (char c : dia) {
            System.out.print(c + ", ");
        }
        System.out.println();
        for (int i = 0; i < board.getBoard().length - rowNum && i < colNum; i++) {
            if (dia[i] != board.getBoard()[rowNum + i][colNum - i])
                fail();
        }
    }

    @Test
    public void arrayWinnerTest() {
        System.out.println("array winner checking");
        char[] xWins = new char[]{nullOnBoard, oOnBoard, xOnBoard, xOnBoard, xOnBoard, xOnBoard, oOnBoard};
        char[] yWins = new char[]{nullOnBoard, nullOnBoard, oOnBoard, oOnBoard, oOnBoard, oOnBoard, oOnBoard};
        char[] noWinner = new char[]{nullOnBoard, nullOnBoard, xOnBoard, oOnBoard, nullOnBoard, xOnBoard, nullOnBoard};
        assertEquals(X_WON, arrayWinner(xWins));
        assertEquals(O_WON, arrayWinner(yWins));
        assertEquals(NO_WINNER, arrayWinner(noWinner));
    }

    @Test
    public void winnerTest() {
        System.out.println("winner checking");
        char[][] xWins = new char[][]{
                {xOnBoard, nullOnBoard, nullOnBoard, xOnBoard, xOnBoard},
                {nullOnBoard, xOnBoard, nullOnBoard, oOnBoard, oOnBoard},
                {nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard, xOnBoard},
                {xOnBoard, nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard}
        };
        board.setBoard(xWins);
        assertEquals(X_WON, board.winner());

        char[][] yWins = new char[][]{
                {nullOnBoard, nullOnBoard, oOnBoard, oOnBoard, oOnBoard},
                {nullOnBoard, nullOnBoard, oOnBoard, xOnBoard, nullOnBoard},
                {nullOnBoard, oOnBoard, xOnBoard, oOnBoard, oOnBoard},
                {oOnBoard, nullOnBoard, oOnBoard, oOnBoard, oOnBoard}
        };
        board.setBoard(yWins);
        assertEquals(O_WON, board.winner());

        char[][] noWinner = new char[][]{
                {nullOnBoard, nullOnBoard, xOnBoard, oOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, oOnBoard, oOnBoard, oOnBoard},
                {nullOnBoard, nullOnBoard, oOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, oOnBoard, oOnBoard},
        };
        board.setBoard(noWinner);
        assertEquals(NO_WINNER, board.winner());

        Board newB = new Board(6, 7);
        char[][] no = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, oOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, oOnBoard, oOnBoard, xOnBoard, nullOnBoard, oOnBoard, oOnBoard},
        };
        newB.setBoard(no);
        assertEquals(NO_WINNER, newB.winner());
    }

    @Test
    public void lowestPlace() {
        System.out.println("lowest in col checking");
        char[][] map = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, oOnBoard, nullOnBoard},
                {nullOnBoard, xOnBoard, nullOnBoard, oOnBoard, nullOnBoard},
                {nullOnBoard, oOnBoard, xOnBoard, oOnBoard, nullOnBoard},
                {xOnBoard, xOnBoard, oOnBoard, xOnBoard, nullOnBoard}
        };
        board.setBoard(map);
        assertEquals(2, board.getLowest(0));
        assertEquals(0, board.getLowest(1));
        assertEquals(1, board.getLowest(2));
        assertEquals(-1, board.getLowest(3));
        assertEquals(3, board.getLowest(4));
    }

    @Test
    public void isLegalCheck() {
        System.out.println("is Legal checking");
        char[][] map = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, oOnBoard, nullOnBoard},
                {nullOnBoard, xOnBoard, nullOnBoard, oOnBoard, nullOnBoard},
                {nullOnBoard, oOnBoard, xOnBoard, oOnBoard, nullOnBoard},
                {xOnBoard, xOnBoard, oOnBoard, xOnBoard, nullOnBoard}
        };
        board.setBoard(map);
        assertFalse(board.isLegal(5));
        assertFalse(board.isLegal(-1));
        assertFalse(board.isLegal(3));
        assertTrue(board.isLegal(0));
        assertTrue(board.isLegal(1));
        assertTrue(board.isLegal(2));
        assertTrue(board.isLegal(4));
    }

    @Test
    public void isFullCheck() {
        System.out.println("is Full checking");
        char[][] full = new char[][]{
                {xOnBoard, oOnBoard, oOnBoard, xOnBoard, xOnBoard},
                {oOnBoard, xOnBoard, oOnBoard, oOnBoard, oOnBoard},
                {oOnBoard, oOnBoard, xOnBoard, oOnBoard, xOnBoard},
                {xOnBoard, oOnBoard, oOnBoard, xOnBoard, oOnBoard}
        };
        board.setBoard(full);
        assertTrue(board.isFull());

        char[][] notFull = new char[][]{
                {xOnBoard, oOnBoard, nullOnBoard, xOnBoard, xOnBoard},
                {oOnBoard, xOnBoard, oOnBoard, oOnBoard, oOnBoard},
                {oOnBoard, oOnBoard, xOnBoard, oOnBoard, xOnBoard},
                {xOnBoard, oOnBoard, oOnBoard, xOnBoard, oOnBoard}
        };
        board.setBoard(notFull);
        assertFalse(board.isFull());
    }

    @Test
    public void setMoveCheck() {
        System.out.println("set move checking");
        char[][] full = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {oOnBoard, xOnBoard, oOnBoard, oOnBoard, oOnBoard},
                {oOnBoard, oOnBoard, xOnBoard, oOnBoard, xOnBoard},
                {xOnBoard, oOnBoard, oOnBoard, xOnBoard, oOnBoard}
        };
        board.setBoard(full);
        int turn = board.getTurn();
        board.setMove(0);
        assertNotEquals(nullOnBoard, board.getBoard()[0][0]);
        assertEquals(turn + 1, board.getTurn());
    }

    @Test
    public void delMoveCheck() {
        System.out.println("delete move checking");
        char[][] full = new char[][]{
                {oOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {oOnBoard, xOnBoard, oOnBoard, oOnBoard, oOnBoard},
                {oOnBoard, oOnBoard, xOnBoard, oOnBoard, xOnBoard},
                {xOnBoard, oOnBoard, oOnBoard, xOnBoard, oOnBoard}
        };
        board.setBoard(full);
        int turn = board.getTurn();
        board.delMove(0);
        assertEquals(nullOnBoard, board.getBoard()[0][0]);
        assertEquals(turn - 1, board.getTurn());
    }

    @Test
    public void calculateLineCheck() {
        System.out.println("caclculate line checking");
        Bot bot = new Bot(board, xOnBoard);
        char[] oneArea = new char[]{nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard, nullOnBoard, nullOnBoard};
        assertEquals(4, bot.calculateLine(oneArea));
        char[] twoArea = new char[]{nullOnBoard, nullOnBoard, xOnBoard, xOnBoard, nullOnBoard, nullOnBoard, nullOnBoard};
        assertEquals(31, bot.calculateLine(twoArea));
        char[] threeArea = new char[]{nullOnBoard, nullOnBoard, xOnBoard, xOnBoard, xOnBoard, nullOnBoard, nullOnBoard};
        assertEquals(220, bot.calculateLine(threeArea));
        char[] fourArea = new char[]{nullOnBoard, xOnBoard, xOnBoard, xOnBoard, xOnBoard, nullOnBoard, nullOnBoard};
        assertEquals(10210, bot.calculateLine(fourArea));
    }

    @Test
    public void calculateCheck() {
        System.out.println("caclculate checking");
        char[][] oneArea = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
        };
        Board board = new Board(6, 7);
        board.setBoard(oneArea);
        Bot bot = new Bot(board, xOnBoard);
        assertEquals(7, bot.calculate());
        char[][] twoArea = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, xOnBoard, nullOnBoard, nullOnBoard},
        };
        board.setBoard(twoArea);
        bot = new Bot(board, xOnBoard);
        assertEquals(36, bot.calculate());
        char[][] threeArea = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, xOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, xOnBoard, nullOnBoard, nullOnBoard},
        };
        board.setBoard(threeArea);
        bot = new Bot(board, xOnBoard);
        assertEquals(110, bot.calculate());
        char[][] fourArea = new char[][]{
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, oOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, nullOnBoard, xOnBoard, nullOnBoard, nullOnBoard, nullOnBoard},
                {nullOnBoard, nullOnBoard, oOnBoard, xOnBoard, nullOnBoard, oOnBoard, nullOnBoard},
        };
        board.setBoard(fourArea);
        bot = new Bot(board, oOnBoard);
        assertEquals(-6, bot.calculate());
    }
}
