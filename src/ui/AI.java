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
		
	public ArrayList<Board> wallPlacementSearch(int player, int opponent){
		ArrayList<Board> moves = new ArrayList<Board>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				int[] theWall = new int[3];
				theWall[0] = i;
				theWall[1] = j;
				theWall[2] = 0;
				if(AIboard.canPlaceWall(theWall)){
					Board tempB = new Board(AIboard);
					tempB.placeWallBoard(theWall);
					moves.add(tempB);
				}
				theWall[2] = 1;
				if(AIboard.canPlaceWall(theWall)){
					Board tempB = new Board(AIboard);
					tempB.placeWallBoard(theWall);
					moves.add(tempB);
				}
			}	
		}return moves;
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
	


