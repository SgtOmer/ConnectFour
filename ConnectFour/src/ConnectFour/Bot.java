package ConnectFour;

public class Bot {
	
	final static char nullOnBoard='0';
	final static char xOnBoard='X';
	final static char oOnBoard='Y';
	Board board;
	char sign;
	char enemy;
	
	public Bot(Board board, char sign){
		this.board=new Board(board);
		this.sign=sign;
		if (sign==xOnBoard){
			enemy=oOnBoard;
		} else
			enemy=xOnBoard;
	}
	
	public int[] makeMove(int turn, boolean bot){
		int bestMove=-1;
		int bestScore = (bot) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int score;
		if (turn == 9 || board.winner() != 0 || board.isFull()) {
			bestScore = calculate();
		} else {
			for (int i=0 ; i<board.board[0].length ; i++){
				if (board.isLegal(i)){
					board.setMove(i);
					score = makeMove(turn+1, !bot)[0];
					if (turn == 0)
						System.out.println("move: "+(i+1)+" score: "+score);
					if (bot){
						if (score > bestScore){
							bestScore = score;
							bestMove = i;
						}
					} else {
						if (score < bestScore){
							bestScore = score;
							bestMove = i;
						}
					}
					board.delMove(i);
				}
			}
		}
		return new int[] {bestScore, bestMove};
	}
	
	public int calculate(){
		int score = 0;
		
		for (int i=0 ; i<board.board.length ; i++){
			score += calculateLine(board.board[i]);
		}
		
		for (int i=0 ; i<board.board[0].length ; i++){
			score += calculateLine(board.getCol(i));
		}
		
		for(int i=0 ; i<board.board[0].length-3 ; i++){
			score += calculateLine(board.getMainDiagnol(0, i));
		}
		
		for(int i=1 ; i<board.board.length-3 ; i++){
			score += calculateLine(board.getMainDiagnol(i,0));
		}
		
		for(int i=board.board[0].length-1 ; i>2 ; i--){
			score += calculateLine(board.getSecDiagnol(0, i));
		}
		
		for(int i=1 ; i<board.board.length-3 ; i++){
			score += calculateLine(board.getSecDiagnol(i, board.board[0].length-1));
		}
		
		return score;
	}
	
	public int calculateLine(char[] line){
		int score = 0;
		
		for (int i=0 ; i<line.length-3 ; i++){
			//four area
			 if (line[i]==line[i+1]&&line[i+2]==line[i+3]&&line[i]==line[i+2]){
				 if (line[i]==sign)
					 score+=10000;
				 else if (line[i]==enemy)
					 score-=10000;
			 }
			 //three area
			 if (line[i]==line[i+1]&&line[i+2]==line[i]&&nullOnBoard==line[i+3]){
				 if (line[i]==sign)
					 score+=100;
				 else if (line[i]==enemy)
					 score-=100;
			 }
			 if (line[i]==line[i+1]&&line[i]==line[i+3]&&nullOnBoard==line[i+2]){
				 if (line[i]==sign)
					 score+=100;
				 else if (line[i]==enemy)
					 score-=100;
			 }
			 if (line[i]==line[i+2]&&line[i+2]==line[i+3]&&nullOnBoard==line[i+1]){
				 if (line[i]==sign)
					 score+=100;
				 else if (line[i]==enemy)
					 score-=100;
			 }
			 if (line[i+2]==line[i+1]&&line[i+2]==line[i+3]&&nullOnBoard==line[i]){
				 if (line[i+1]==sign)
					 score+=100;
				 else if (line[i+1]==enemy)
					 score-=100;
			 }
			 //two area
			 if (line[i]==line[i+1]&&line[i+2]==line[i+3]&&nullOnBoard==line[i+2]){
				 if (line[i]==sign)
					 score+=10;
				 else if (line[i]==enemy)
					 score-=10;
			 }
			 if (line[i]==line[i+3]&&line[i+2]==line[i+1]&&nullOnBoard==line[i+2]){
				 if (line[i]==sign)
					 score+=10;
				 else if (line[i]==enemy)
					 score-=10;
			 }
			 if (line[i]==line[i+1]&&line[i+2]==line[i+3]&&nullOnBoard==line[i]){
				 if (line[i+2]==sign)
					 score+=10;
				 else if (line[i+2]==enemy)
					 score-=10;
			 }
			 if (line[i+3]==line[i+1]&&line[i+2]==line[i]&&nullOnBoard==line[i+2]){
				 if (line[i+1]==sign)
					 score+=10;
				 else if (line[i+1]==enemy)
					 score-=10;
			 }
			 if (line[i+2]==line[i+1]&&line[i]==line[i+3]&&nullOnBoard==line[i]){
				 if (line[i+1]==sign)
					 score+=10;
				 else if (line[i+1]==enemy)
					 score-=10;
			 }
			 if (line[i]==line[i+2]&&line[i+1]==line[i+3]&&nullOnBoard==line[i+1]){
				 if (line[i]==sign)
					 score+=10;
				 else if (line[i]==enemy)
					 score-=10;
			 }
			 //one area
			 if (line[i+2]==line[i+1]&&line[i+2]==line[i+3]&&line[i+1]==nullOnBoard){
				 if (line[i]==sign)
					 score+=1;
				 else if (line[i]==enemy)
					 score-=1;
			 }
			 if (line[i]==line[i+2]&&line[i+2]==line[i+3]&&line[i]==nullOnBoard){
				 if (line[i+1]==sign)
					 score+=1;
				 else if (line[i+1]==enemy)
					 score-=1;
			 }
			 if (line[i]==line[i+1]&&line[i+1]==line[i+3]&&line[i]==nullOnBoard){
				 if (line[i+2]==sign)
					 score+=1;
				 else if (line[i+2]==enemy)
					 score-=1;
			 }
			 if (line[i]==line[i+1]&&line[i+2]==line[i]&&line[i]==nullOnBoard){
				 if (line[i+3]==sign)
					 score+=1;
				 else if (line[i+3]==enemy)
					 score-=1;
			 }
		}
		
		return score;
	}
}
