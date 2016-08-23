package ConnectFour;

public class Board{
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='O';
	public char[][] board;
	int cols;
	int rows;
	int turn;
	
	public Board(int rows, int cols){
		board = new char[rows][cols];
		this.cols=cols;
		this.rows=rows;
		turn=0;
	}
	
	public Board(Board board){
		cols=board.board[0].length;
		rows=board.board.length;
		turn=board.turn;
		this.board = new char[rows][cols];
		for (int i=0 ; i<rows ; i++){
			for (int j=0 ; j<cols ; j++){
				this.board[i][j] = board.board[i][j];
			}
		}
	}
	
	public  void reset(){
		for (int i=0 ; i<board.length ; i++){
			for (int j=0 ; j<board[i].length ; j++){
				board[i][j] = nullOnBoard;
			}
		}
		turn=0;
	}
	
	public char[] getCol(int index){
		char[] col = new char[board.length];
		for (int i=0 ; i<board.length ; i++)
			col[i] = board[i][index];
		return col;
	}
	
	public char[] getMainDiagnol(int row, int col){
		char[] diagnol = new char[Math.min(board.length-row, board[0].length-col)];
		for (int i=0 ; i<diagnol.length ; i++)
			diagnol[i] = board[row+i][col+i];
		return diagnol;
	}
	
	public char[] getSecDiagnol(int row, int col){
		char[] diagnol = new char[Math.min(board.length-row, col+1)];
		for (int i=0 ; i<diagnol.length ; i++)
			diagnol[i] = board[row+i][col-i];
		return diagnol;
	}
	
	public int winner(){
		int winner;
		
		for (int i=0 ; i<rows ; i++){
			winner = arrayWinner(board[i]);
			if (winner!=0)
				return winner;
		}
		
		for (int i=0 ; i<cols ; i++){
			winner = arrayWinner(getCol(i));
			if (winner!=0)
				return winner;
		}
		
		for(int i=0 ; i<cols-3 ; i++){
			winner = arrayWinner(getMainDiagnol(0, i));
			if (winner!=0)
				return winner;
		}
		
		for(int i=1 ; i<rows-3 ; i++){
			winner = arrayWinner(getMainDiagnol(i, 0));
			if (winner!=0)
				return winner;
		}
		
		for(int i=cols-1 ; i>2 ; i--){
			winner = arrayWinner(getSecDiagnol(0, i));
			if (winner!=0)
				return winner;
		}
		
		for(int i=1 ; i<rows-3 ; i++){
			winner = arrayWinner(getSecDiagnol(i, cols-1));
			if (winner!=0)
				return winner;
		}
		
		return 0;
	}
	
	public int getLowest(int col){
		int row=-1;
		for (int i=rows-1 ; i>-1 ; i--){
			if (board[i][col]==nullOnBoard)
				return i;
		}
		return row;
	}
	
	public boolean isLegal(int  move){
		if (move>cols-1||move<0)
			return false;
		if (getLowest(move)==-1)
			return false;
		return true;
	}
	
	public boolean setMove(int move){
		if (isLegal(move)){
			int lowest = getLowest(move);
			if(turn%2==0)
				board[lowest][move]='X';
			else
				board[lowest][move]='O';
			turn++;
			return true;
		}
		return false;
	}
	
	public boolean isFull(){
		for (int i=0 ; i<cols ; i++){
			if (isLegal(i))
				return false;
		}
		return true;
	}
	
	public void delMove(int move){
		int lowest=getLowest(move);
		board[lowest+1][move]=nullOnBoard;
		turn--;
	}
	
	public void show(){
		for (int i=0 ; i<board.length ; i++){
			for (int j=0 ; j<board[i].length ; j++){
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
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
}
