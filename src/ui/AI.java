package src.ui;
import src.ui.Board;
//import src.ui.PlayQuor;

public class AI{

	
	   final int NUMPLAY;
	   final int WALL = 5;
	   int[][] grid = new int[17][17];
	   int[] move = new int[4];


	public AI(int players){
		this.grid = new int[17][17];
		setAI(players);
		if(players == 4)
			NUMPLAY = 4;
		else
			NUMPLAY = 2;
	}
		
	public AI(AI a){
		 this.grid = a.grid;
	     this.NUMPLAY = a.NUMPLAY;
	}
	
	public void setAI(int players){
		for(int i = 0; i < 17; i++){
			for(int j = 0; j < 17; j++){
				grid[i][j] = 0;
			}
		}
		grid[8][0] = 1;
		grid[8][16] = 2;
		if(players == 4){
			grid[0][8] = 3;
			grid[16][8] = 4;
		}
		for(int i = 0; i < 17; i++){
			for(int j = 0; j < 17; j++){
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void AIMove(int numOfPlayers, int player){
	     currentLoc(numOfPlayers);
	     moves(player);
		 }
	
	public void currentLoc(int numOfPlayers){
		if(numOfPlayers==2){
			playerPlace(1);
			playerPlace(2);
		}else if(numOfPlayers==4){
			playerPlace(1);
			playerPlace(2);
			playerPlace(3);
			playerPlace(4);
		}
	}
	
	public int[] playerPlace(int Player){
			int[] location = new int[2];
			for(int col = 0; col < 17; col++){
				for(int row = 0; row < 17; row++){
					if(grid[col][row] == Player){
						location[0] = col;
						location[1] = row;
						break;
				}
			}
		}
		return location;
	}

	public int[] moves(int playerTurn){
			move = a.bestMove(1);
			grid[move[3]][move[4]]=grid[move[0]][move[1]];
			grid[move[0]][move[1]]= 0;
			return move;
	}
		
	}	

	


