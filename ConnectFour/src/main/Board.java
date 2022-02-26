package main;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

import static main.Utils.NULL_ON_BOARD;
import static main.Utils.X_ON_BOARD;
import static main.Utils.O_ON_BOARD;

@Getter
@Setter
public class Board /*implements ActionListener, Runnable, MouseListener*/ {
    private char[][] board;
    private int cols;
    private int rows;
    private int turn;

    public final int WIDTH = 1600, HEIGHT = 1600, BOX = 200;
//	public Renderer renderer;

    public Board(int rows, int cols) {
        board = new char[rows][cols];
        this.cols = cols;
        this.rows = rows;
        turn = 0;

		/*JFrame jframe = new JFrame();

		Timer timer = new Timer(20, this);

		renderer = new Renderer();

		jframe.add(renderer);
		jframe.setTitle("Connect Four");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addMouseListener(this);
		jframe.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		jframe.setResizable(true);
		jframe.pack();
		jframe.setVisible(true);

		timer.start();*/
    }

    public Board(Board board) {
        cols = board.board[0].length;
        rows = board.board.length;
        turn = board.turn;
        this.board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(board.board[i], 0, this.board[i], 0, cols);
        }
    }

    public void reset() {
        for (char[] chars : board) {
            Arrays.fill(chars, NULL_ON_BOARD);
        }
        turn = 0;
    }

    public char[] getCol(int index) {
        char[] col = new char[board.length];
        for (int i = 0; i < board.length; i++)
            col[i] = board[i][index];
        return col;
    }

    public char[] getMainDiagonal(int row, int col) {
        char[] diagonal = new char[Math.min(board.length - row, board[0].length - col)];
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = board[row + i][col + i];
        return diagonal;
    }

    public char[] getSecDiagonal(int row, int col) {
        char[] diagonal = new char[Math.min(board.length - row, col + 1)];
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = board[row + i][col - i];
        return diagonal;
    }

    public int winner() {
        int winner;

        for (int i = 0; i < rows; i++) {
            winner = arrayWinner(board[i]);
            if (winner != 0)
                return winner;
        }

        for (int i = 0; i < cols; i++) {
            winner = arrayWinner(getCol(i));
            if (winner != 0)
                return winner;
        }

        for (int i = 0; i < cols - 3; i++) {
            winner = arrayWinner(getMainDiagonal(0, i));
            if (winner != 0)
                return winner;
        }

        for (int i = 1; i < rows - 3; i++) {
            winner = arrayWinner(getMainDiagonal(i, 0));
            if (winner != 0)
                return winner;
        }

        for (int i = cols - 1; i > 2; i--) {
            winner = arrayWinner(getSecDiagonal(0, i));
            if (winner != 0)
                return winner;
        }

        for (int i = 1; i < rows - 3; i++) {
            winner = arrayWinner(getSecDiagonal(i, cols - 1));
            if (winner != 0)
                return winner;
        }

        return 0;
    }

    public int getLowest(int col) {
        int row = -1;
        for (int i = rows - 1; i > -1; i--) {
            if (board[i][col] == NULL_ON_BOARD)
                return i;
        }
        return row;
    }

    public boolean isLegal(int move) {
        if (move > cols - 1 || move < 0)
            return false;

        return getLowest(move) != -1;
    }

    public boolean setMove(int move) {
        if (isLegal(move)) {
            int lowest = getLowest(move);
            if (turn % 2 == 0)
                board[lowest][move] = X_ON_BOARD;
            else
                board[lowest][move] = O_ON_BOARD;
            turn++;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        for (int i = 0; i < cols; i++) {
            if (isLegal(i))
                return false;
        }
        return true;
    }

    public void delMove(int move) {
        int lowest = getLowest(move);
        board[lowest + 1][move] = NULL_ON_BOARD;
        turn--;
    }

    public void show() {
        for (char[] chars : board) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    public static int arrayWinner(char[] array) {
        for (int i = 0; i < array.length - 3; i++) {
            if (array[i] == array[i + 1] && array[i + 2] == array[i + 3] && array[i] == array[i + 2]) {
                if (array[i] == X_ON_BOARD)
                    return 1;
                else if (array[i] == O_ON_BOARD)
                    return 2;
            }
        }
        return 0;
    }
	
	/*public void repaint(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		for (int i = 0 ; i < 7 ; i++){
			g.drawLine(100, 200 + i * 200, WIDTH - 100, 200 + i * 200);
		}
		
		for (int i = 0 ; i < 8 ; i++){
			g.drawLine(100 + i * 200, 200, 100 + i * 200, HEIGHT - 200);
		}
		
		g.setFont(new Font("Arial", 1, 150));
		for (int i = 0 ; i < board.length ; i++){
			for (int j=0 ; j<board[0].length ; j++){
				if (board[i][j]!=nullOnBoard){
					g.drawString(String.valueOf(board[i][j]), 100+j*200+50, 200+i*200+150);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		renderer.repaint();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (y > 200 && y < HEIGHT - 200){
			if (x < WIDTH - 100 && x > 100){
				switch ((x-100)/200){
					case 0:
						setMove(0);
						System.out.println(1);
						break;
					case 1:
						setMove(1);
						System.out.println(2);
						break;
					case 2:
						setMove(2);
						System.out.println(3);
						break;
					case 3:
						setMove(3);
						System.out.println(4);
						break;
					case 4:
						setMove(4);
						System.out.println(5);
						break;
					case 5:
						setMove(5);
						System.out.println(6);
						break;
					case 6:
						setMove(6);
						System.out.println(7);
						break;
				}
			}
		}
	}*/
}
