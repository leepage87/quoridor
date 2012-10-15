package src.ui;
import src.ui.Board;

public class AI{

	
	   final int NUMPLAY;
	   final int WALL = 5;
	   int[][] grid;
	   Board x = new Board(2);

		 
	// Parameters: the number of players (2 or 4)
	// Creates: a board object
	public AI(int players){
		 this.grid = new int[17][17];
		 	setBoard(players);
		        if(players == 4)
		            NUMPLAY = 4;
		        else
		            NUMPLAY = 2;
		    }

  // Parameters: a board
 // Creates: a board object that is a duplicate of the board given
	public AI(AI b){
		  this.grid = b.grid;
	      this.NUMPLAY = b.NUMPLAY;
		    }

		    // Parameters: the number of players (2 or 4)
		    // PostCondition: the board is empty and two or four players exist
	public void setBoard(int players){
		  for(int i = 0; i < 17; i++){
		      for(int j = 0; j < 17; j++){
		          grid[i][j] = 0;
		       }
		   }
	    }
	
	public void main(String[]args){
		int i = 1;
		x.playerPlace(i);
		
	}
	}

	


