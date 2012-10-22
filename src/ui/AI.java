package src.ui;
import java.util.ArrayList;

import src.ui.Board;
import src.ui.PlayQuor;

public class AI{

	
	   final int WALL = 5;
	   int[] move = new int[4];
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
		
	public void wallPlacementSearch(int player, int opponent){
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
		}
		
		
		
		
		
	}
}	

	


