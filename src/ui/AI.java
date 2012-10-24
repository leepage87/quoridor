package src.ui;
import java.util.ArrayList;

import src.ui.Board;
import src.ui.PlayQuor;

public class AI{

	
	   final int WALL = 5;
	   int[] move = new int[5];
	   Board AIboard;

	 public AI(Board b){
         AIboard = b;
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

	// Parameters: the player whose turn it is and the enemy closest to winning
	// Returns: the board after making the best possible move
	private Board findBestMove(int turn, int enemy){
		ArrayList<Board> posMoves = wallPlacementSearch();
		for(int i = 0; i < 4; i++){
			Board nextStep = oneStep(turn, i);
			if(nextStep != AIboard)
				posMoves.add(nextStep);
		}
		Board finalMove = new Board(posMoves.get(0));
		int value = boardValue(turn, enemy, posMoves.get(0));
		for(int i = 1; i < posMoves.size(); i++)
			if(value < boardValue(turn, enemy, posMoves.get(i)))
				finalMove = posMoves.get(i);
		return finalMove;
	}
	
	// Parameters: the player whose turn it is, the enemy closest to winning, and the board being examined
	// Returns: number of moves it will take the enemy to win minus the number of moves it will take the player to win
	private int boardValue(int turn, int enemy, Board b){
		int enemyMoves = b.doSearch(enemy)[2];
		int playerMoves = b.doSearch(turn)[2];
		return enemyMoves-playerMoves;
	}
		
	// Parameters: the player
	// Returns: the enemy who is closest to winning
	private int findEnemy(int turn){
		if(AIboard.NUMPLAY == 2)
			return (turn%2)+1;
		int enemy = (turn%4)+1;
		for(int i = 1; i < 5; i++)
			if((i!= turn) && (AIboard.bestMove(i)[2] < AIboard.bestMove(enemy)[2]))
				enemy = i;
		return enemy;
	}
	
	// Parameters: the player and an int representing a direction
	// Returns: the board after moving the player in the chosen direction, if possible
	private Board oneStep(int turn, int dir){
		Board temp = new Board(AIboard);
		if((dir == 0) && AIboard.canMovePiece('N', turn))
			temp.movePieceBoard('N', turn);
		else if((dir == 1) && AIboard.canMovePiece('S', turn))
			temp.movePieceBoard('S', turn);
		else if((dir == 2) && AIboard.canMovePiece('E', turn))
			temp.movePieceBoard('E', turn);
		else if((dir == 3) && AIboard.canMovePiece('W', turn))
			temp.movePieceBoard('W', turn);
		return temp;
	}
	
	// Returns: an ArrayList of one board for every possible wall placement
	private ArrayList<Board> wallPlacementSearch(){
		// TODO: account for the possibility of running out of walls; link to PlayQuor?
		ArrayList<Board> posMoves = new ArrayList<Board>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				int[] theWall = new int[3];
				theWall[0] = i;
				theWall[1] = j;
				theWall[2] = 0;
				if(AIboard.canPlaceWall(theWall)){
					Board tempB = new Board(AIboard);
					tempB.placeWallBoard(theWall);
					posMoves.add(tempB);
				}
				theWall[2] = 1;
				if(AIboard.canPlaceWall(theWall)){
					Board tempB = new Board(AIboard);
					tempB.placeWallBoard(theWall);
					posMoves.add(tempB);
				}
			}	
		}return posMoves;
	}

	//Testing purposes
		public Board placeWall(){
			Board temp = new Board(AIboard);
			int [] placement = new int[3];
			placement[0] = 1;
			placement[1] = 5;
			placement[3] = 0;
			temp.placeWallBoard(placement);
			return temp;
		}	
}
	


