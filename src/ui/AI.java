package src.ui;
import src.ui.Board;
//import src.ui.PlayQuor;

public class AI{

	
	   final int NUMPLAY;
	   final int WALL = 5;
	   int[][] grid = new int[17][17];
	   Board x = new Board(2);
	   int[] move = new int[4];

  // Parameters: a board
 // Creates: a board object that is a duplicate of the board given
	public AI(Board b, int player, int numOfPlayers){
		  this.grid = b.grid;
	      this.NUMPLAY = b.NUMPLAY;
	      currentLoc(numOfPlayers);
	      move(player);
		    }
	
	public void currentLoc(int numOfPlayers){
		if(numOfPlayers==2){
			x.playerPlace(1);
			x.playerPlace(2);
		}else if(numOfPlayers==4){
			x.playerPlace(1);
			x.playerPlace(2);
			x.playerPlace(3);
			x.playerPlace(4);
		}
	}
	public void move(int playerTurn){
		while(playerTurn == 1){
			move = x.bestMove(1);
		}for(int i = 4; i<=4; i++){
			grid[move[3]][move[4]]=grid[move[0]][move[1]];
			grid[move[0]][move[1]]= 0;
		}
		
	}	
}	

	


