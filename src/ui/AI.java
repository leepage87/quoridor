package src.ui;
import java.util.ArrayList;

import src.ui.Board;
import src.ui.PlayQuor;

public class AI{

	   int[] move = new int[5];
	   Board AIboard;
	   int bigAlpha; 
	   int bigBeta;

	 public AI(Board b){
         AIboard = b;
         bigAlpha = 201;
         bigBeta = -201;
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
	
	// Parameters: the player, the enemy, the current board, the number of turns ahead to be explored
	// Returns: the int of the worst possible outcome for board value
	public int aiMoveB(int player, int enemy, Board b, int numRounds){
		ArrayList<Board> allMoves = findMovesB(player, enemy, b);
		int worst = 200;
		if(numRounds == 0){
			for(int i = 0; i < allMoves.size(); i++){
				int value = boardValue(player, enemy, allMoves.get(i));
				if(value < worst)
					worst = value;
				if(value <= bigBeta)
					return value;
			}
			if(bigBeta == -201)
				bigBeta = worst;
			return worst;
		}
		for(int i = 0; i < allMoves.size(); i++){
			int next = aiMoveB(enemy, player, b, numRounds-1);
			if(next < worst)
				worst = next;
			if(next <= bigBeta)
				return worst;
		}
		return worst;
	}
	
	// Parameters: the player, the enemy, and the current board
	// Returns: an Array of all possible moves
	public ArrayList<Board> findMovesB(int turn, int enemy, Board b){
		ArrayList<Board> posMoves = new ArrayList<Board>();
		if(b.playerWalls[turn-1] != 0)
			posMoves = wallPlacementSearch(b);
		for(int i = 0; i < 17; i=i+2){
				for(int j =0; j < 17; j=j+2){
					int[] destination = new int[2];
					destination[0] = j;
					destination[1] = i;
					Board nextStep = eachStep(turn, destination, b);
					if(nextStep != b)
						posMoves.add(nextStep);
				}
		}
		return posMoves;
	}
	
	
	// Parameters: the player whose turn it is
	// PostCondition: the best move is made
	public Board aiMove(int turn){ //TODO: look turns ahead. Recursive in findBestMove?
		Board myMove = new Board(AIboard);
		Board currentBoard = new Board(AIboard);
		int tempTurn = turn;
		for(int i = 0; i < 1; i++){ // # of turns ahead
			int enemy = findEnemy(turn, currentBoard);
			currentBoard = findBestMove(turn, enemy, currentBoard);
			if(i == 0)
				myMove = currentBoard;
			tempTurn = (tempTurn%4)+1;
		}
		return myMove;
	}
	
	// Parameters: the player whose turn it is and the enemy closest to winning
	// Returns: the board after making the best possible move
	public Board findBestMove(int turn, int enemy, Board b){
		ArrayList<Board> posMoves = new ArrayList<Board>();
		if(b.playerWalls[turn-1] != 0)
			posMoves = wallPlacementSearch(b);
		for(int i = 0; i < 17; i=i+2){
				for(int j =0; j < 17; j=j+2){
					int[] destination = new int[2];
					destination[0] = j;
					destination[1] = i;
					Board nextStep = eachStep(turn, destination, b);
					if(nextStep != b)
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
	
	// Parameters: the player whose turn it is, the enemy closest to winning, and the board being examined
	// Returns: number of moves it will take the enemy to win minus the number of moves it will take the player to win
	public int boardValue(int turn, int enemy, Board b){
		if(b.haveWon())
			return 200;
		int enemyMoves = b.doSearch(enemy)[2];
		int playerMoves = b.doSearch(turn)[2];
		int walls = 2*b.playerWalls[turn-1];
		return enemyMoves-playerMoves+walls;
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
	


