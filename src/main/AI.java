package src.main;
import java.util.ArrayList;


public class AI{

	   int[] move = new int[5];
	   Board AIboard;
	   public int beta;
	   public int truePlayer;
	   private int[] playerWalls;

	 public AI(Board b){
         AIboard = b;
         beta = -201;
	 }

	public void AIMove(int player){
	     moves(currentLoc(player), player);
		 }
	
	public int[] currentLoc(int player){
		int[] place = new int[2];
			place = AIboard.playerPlace(player);
			return place;
	}
	

	public int[] moves(int [] place, int player){
			if(!AIboard.haveWon())
				move = AIboard.bestMove(player);
			return move;
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
	public Board aiMoveB(int player){
		truePlayer = player;
		int rounds = 1;
		ArrayList<Board> moves = findMovesB(player, AIboard);
		int enemy = findEnemy(player, AIboard);
		int best = aiMoveB(player, enemy, moves.get(0), rounds);
		int whichBoard = 0;
		for(int i = 1; i < moves.size(); i++){
			int value = aiMoveB((player%AIboard.NUMPLAY)+1, player, moves.get(i), rounds);
			if(value > best){
				best = value;
				whichBoard = i;
			}
		}
		return moves.get(whichBoard);
	}
	
	// Parameters: the player, the enemy, the current board,
	//   and the number of turns ahead to be explored
	// Returns: the board value of the best possible outcome
	public int aiMoveB(int player, int enemy, Board b, int numRounds){
		ArrayList<Board> allMoves = findMovesB(player, b);
		if(numRounds == 0)
			return baseCase(player, enemy, allMoves);
		return search(player, enemy, allMoves, numRounds);
	}
	
	// Parameters:
	// Returns:
	public int search(int player, int enemy, ArrayList<Board> allMoves, int numRounds){
		int best = -200;
		for(int i = 0; i < allMoves.size(); i++){
			int realValue;
			if((player%AIboard.NUMPLAY)+1==truePlayer){
				int nextEnemy = findEnemy(truePlayer, AIboard);
				realValue = aiMoveB(player, nextEnemy, allMoves.get(i), numRounds-1);
			}else
				realValue = aiMoveB((player%AIboard.NUMPLAY)+1, player, allMoves.get(i), numRounds-1);
			int value = realValue;
			if(player != truePlayer)
				value *= -1;
			if(value > best){
				best = value;
			}
			if(realValue <= beta)
				return realValue;
		}
		return best;
	}
	
	// Parameters:
	// Returns:
	public int baseCase(int player, int enemy, ArrayList<Board> allMoves){
		int best = -200;
		for(int i = 0; i < allMoves.size(); i++){
			int realValue = boardValue(player, enemy, allMoves.get(i));
			int value = realValue;
			if(player != truePlayer)
				value *= -1;
			if(value > best)
				best = value;
			if(realValue <= beta)
				return realValue;
		}
		if(player != truePlayer)
			best *= -1;
		if(beta == -201)
			beta = best;
		return best;
	}
	
	// Parameters: the player, the enemy, and the current board
	// Returns: an Array of all possible moves
	public ArrayList<Board> findMovesB(int turn, Board b){
		ArrayList<Board> posMoves = new ArrayList<Board>();
		playerWalls = b.getPlayerWalls();
		if(playerWalls[turn-1] != 0)
			posMoves = wallPlacementSearch(b);
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
	
	
	// Parameters: the player whose turn it is
	// PostCondition: the best move is made
	public Board aiMove(int turn){
		Board myMove = new Board(AIboard);
		int enemy = findEnemy(turn, myMove);
		return findBestMove(turn, enemy, myMove);
	}
	
	// Parameters: the player whose turn it is and the enemy closest to winning
	// Returns: the board after making the best possible move
	public Board findBestMove(int turn, int enemy, Board b){
		ArrayList<Board> posMoves = new ArrayList<Board>();
		playerWalls = b.getPlayerWalls();
		if(playerWalls[turn-1] != 0)
			posMoves = wallPlacementSearch(b);
		for(int i = 0; i < 17; i=i+2){
				for(int j =0; j < 17; j=j+2){
					int[] destination = new int[2];
					destination[0] = j;
					destination[1] = i;
					Board nextStep = eachStep(turn, destination, b);
					if(!nextStep.equals(b))
						posMoves.add(nextStep);
			}
		}
		Board finalMove = new Board(posMoves.get(0));
		int value = boardValue(turn, enemy, posMoves.get(0));
		for(int i = 1; i < posMoves.size(); i++)
			if(value < boardValue(turn, enemy, posMoves.get(i))){
				finalMove = posMoves.get(i);
				value = boardValue(turn, enemy, posMoves.get(i));
			}
		return finalMove;
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
		playerWalls = b.getPlayerWalls();
		int walls = 2*playerWalls[turn-1];
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
	public ArrayList<Board> wallPlacementSearch(Board b){
		ArrayList<Board> posMoves = new ArrayList<Board>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				int[] theWall = new int[3];
				theWall[0] = i;
				theWall[1] = j;
				theWall[2] = 0;
				if(b.canPlaceWall(theWall)){
					Board tempB = new Board(b);
					tempB.placeWallBoard(theWall);
					posMoves.add(tempB);
				}
				theWall[2] = 1;
				if(b.canPlaceWall(theWall)){
					Board tempB = new Board(b);
					tempB.placeWallBoard(theWall);
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
        AIboard.placeWallBoard(placement);
        return AIboard;
}       


	//Testing purposes
		public Board move(int [] placement, int player){
			Board temp = new Board(AIboard);
			temp.quickMove(placement, 1);
			return temp;
		}	
}
	


