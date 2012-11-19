package src.main;
import java.util.ArrayList;


public class AI{

	   int[] move = new int[5];
	   Board AIboard;
	   public int truePlayer;
	   private int[] playerWalls;

	 public AI(Board b){
         AIboard = b;
	 }

	
    // Paramters: the player, the board before moving, the board after moving
    // Returns: where the player placed a wall
	public int[] aiWall(int player, Board old, Board current){
		int[] startPlace = old.playerPlace(player);
		int[] endPlace = current.playerPlace(player);
		if(startPlace[0]!=endPlace[0] || startPlace[1]!=endPlace[1])
			return null;
		int[] aiWall = new int[3];
		boolean getOut = false;
		for(int col = 1; col < 17; col=col+2){
			for(int row = 1; row < 17; row=row+2){		
				if(old.grid[col][row] != current.grid[col][row]){
					aiWall[0] = col/2;
					aiWall[1] = row/2;
					if(current.grid[col][row+1]==5)
						aiWall[2] = 1;
					getOut = true;
				}
				if(getOut)
					break;
			}
			if(getOut)
				break;
		}
		return aiWall;
	}
	
	// Parameters: the player
	// Returns: the board after moving
	public Board aiMove(int player, int rounds){
		truePlayer = player;
		int[] answer = new int[3];
		answer[0] = -201;
		answer[1] = 201;
		answer[2] = -201;
		ArrayList<Board> moves = findMoves(player, AIboard);
		ArrayList<Integer> goodMoves = new ArrayList<Integer>();
		if(rounds == 0){
			int enemy = findEnemy(player, AIboard);
			int value = boardValue(player, enemy, moves.get(0));
			for(int i = 1; i < moves.size(); i++){
				int nextValue = boardValue(player, enemy, moves.get(i));
				if(nextValue > value){
					value = nextValue;
					System.out.println(moves.get(i));
					goodMoves = new ArrayList<Integer>();
					goodMoves.add(i);
				}else if(nextValue == value)
					goodMoves.add(i);
			}
			int whichBoard = goodMoves.get((int) (Math.random() * goodMoves.size()));
			return moves.get(whichBoard);
		}
		int[] next = search(answer, (player%AIboard.NUMPLAY)+1, moves.get(0), rounds-1);
		answer[0] = next[0];
		answer[2] = next[0];
		goodMoves.add(0);
		for(int i = 1; i < moves.size(); i++){
			next = search(answer, (player%AIboard.NUMPLAY)+1, moves.get(i), rounds-1);
			if(next[0] > answer[0]){
				goodMoves = new ArrayList<Integer>();
				answer[0] = next[0];
				answer[2] = next[0];
				goodMoves.add(i);
			}else if(next[0] == answer[0]){
				goodMoves.add(i);
			}
		}
		if(goodMoves.size() > 8)
			return aiMove(player, 0);
		int whichBoard = goodMoves.get((int) (Math.random() * goodMoves.size()));			
		return moves.get(whichBoard);
	}

	// Parameters: an array of the current value + the alpha + the beta,
	//    the player, a board, and the number of rounds to look ahead
	// Returns: the same array, or the same array with either the alpha or beta changed
	public int[] search(int[] answer, int player, Board b, int numRounds){
		ArrayList<Board> allMoves = findMoves(player, b);
		if(numRounds < 1)
			return baseCase(answer, player, allMoves);
		int[] nextAnswer = {answer[0], answer[1], answer[2]};
		if(player == truePlayer){
			int best = -201;
			for(int i = 0; i < allMoves.size(); i++){
				int[] value = search(nextAnswer, (player%AIboard.NUMPLAY)+1, allMoves.get(i), numRounds-1);
				if(value[0] > nextAnswer[2])
					nextAnswer[2] = value[0];
				if(value[0] > nextAnswer[1]){
					return nextAnswer; 
				}
			}
			int[] newAnswer = {best, answer[1], answer[2]};
			return newAnswer;
		}
		int worst = 201;
		for(int i = 0; i < allMoves.size(); i++){
			int[] value = search(nextAnswer, (player%AIboard.NUMPLAY)+1, allMoves.get(i), numRounds-1);
			if(value[0] < nextAnswer[1])
				nextAnswer[2] = value[0];
			if(value[0] < nextAnswer[2]){
				return nextAnswer; 
			}
		}
		int[] newAnswer = {worst, answer[1], answer[2]};
		return newAnswer;
	}
	
	
	// Parameters: an array of the current value + the alpha + the beta,
	//    the player, and an ArrayList of possible boards
	// Returns: an array of the best move, or a move that is lower than the beta, or a
	//    move that is higher than the alpha + the alpha + the beta
	public int[] baseCase(int[] answer, int player, ArrayList<Board> allMoves){
		if(player == truePlayer){
			int best = -201;
			for(int i = 0; i < allMoves.size(); i++){
				int enemy = findEnemy(player, AIboard);
				int value = boardValue(player, enemy, allMoves.get(i));
				if(value > best)
					best = value;
				if(value > answer[1]){
					int[] newAnswer = {value, answer[1], answer[2]};
					return newAnswer;
				}
			}
			int[] newAnswer = {best, answer[1], answer[2]};
			return newAnswer;
		}
		int worst = 201;
		for(int i = 0; i < allMoves.size(); i++){
			int value = boardValue(truePlayer, player, allMoves.get(i));
			if(value < worst)
				worst = value;
			if(value < answer[2]){
				int[] newAnswer = {value, answer[1], answer[2]};
				return newAnswer;
			}
		}
		int[] newAnswer = {worst, answer[1], answer[2]};
		return newAnswer;
	}
	
	// Parameters: the player, the enemy, and the current board
	// Returns: an Array of all possible moves
	public ArrayList<Board> findMoves(int turn, Board b){
		ArrayList<Board> posMoves = new ArrayList<Board>();
		if(b.playerWalls[turn-1] != 0)
			posMoves = wallPlacementSearch(b, turn);
		for(int row = 0; row < 17; row=row+2){
				for(int col =0; col < 17; col=col+2){
					int[] destination = new int[2];
					destination[0] = col;
					destination[1] = row;
					Board nextStep = eachStep(turn, destination, b);
					if(!nextStep.equals(b))
						posMoves.add(nextStep);
				}
		}
		return posMoves;
	}

	// Parameters: the player whose turn it is, the enemy closest
	//   to winning, and the board being examined
	// Returns: number of moves it will take the enemy to win minus
	//   the number of moves it will take the player to win
	//   plus 2*the number of remaining walls
	public int boardValue(int turn, int enemy, Board b){
		if(b.haveWon())
			return 200;
		int enemyMoves = b.doSearch(enemy)[2];
		int playerMoves = b.doSearch(turn)[2];
		int walls = 2*b.playerWalls[turn-1];
		return enemyMoves-playerMoves;
	}
		
	// Parameters: the player
	// Returns: the enemy who is closest to winning
	public int findEnemy(int turn, Board b){
		if(b.NUMPLAY == 2)
			return (turn%2)+1;
		int enemy = (turn%4)+1;
		for(int i = 1; i < 5; i++)
			if((i!= turn) && (b.bestMove(i)[2] < b.bestMove(enemy)[2]))
				enemy = i;
		return enemy;
	}
	
	// Parameters: the player and an int representing a direction
	// Returns: the board after moving the player in the chosen direction, if possible
	public Board eachStep(int turn, int[] destination, Board b){	
		Board temp = new Board(b);
		if(temp.aiCanMove(destination, turn))
				temp.quickMove(destination, turn);
		return temp;
	}
	
	// Returns: an ArrayList of one board for every possible wall placement
	public ArrayList<Board> wallPlacementSearch(Board b, int player){
		ArrayList<Board> posMoves = new ArrayList<Board>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				int[] theWall = new int[3];
				theWall[0] = i;
				theWall[1] = j;
				theWall[2] = 0;
				if(b.canPlaceWall(theWall, player)){
					Board tempB = new Board(b);
					tempB.placeWallBoard(theWall, player);
					posMoves.add(tempB);
				}
				theWall[2] = 1;
				if(b.canPlaceWall(theWall, player)){
					Board tempB = new Board(b);
					tempB.placeWallBoard(theWall, player);
					posMoves.add(tempB);
				}
			}	
		}return posMoves;
	}
	//Testing purposes
	public Board placeWall(){
        int [] placement = new int[3];
        placement[0] = 1;
        placement[1] = 1;
        AIboard.placeWallBoard(placement, 1);
        return AIboard;
}       


	//Testing purposes
		public Board move(int [] placement, int player){
			Board temp = new Board(AIboard);
			temp.quickMove(placement, 1);
			return temp;
		}	
		
		//Testing purposes
		public int[] playerPlacee(int player){
			int [] place = new int[2];
			place = AIboard.playerPlace(player);
			return place;
		}
}
	


