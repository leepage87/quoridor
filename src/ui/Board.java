/**
 * Tim Simmons
 * teamOrangeBeard
 */
package src.ui;

public class Board {

    final static int P1 = 1;
    final static  int P2 = 2;
    final static int P3 = 3;
    final static int P4 = 4;
    final static int WALL = 5;

    static int[][] grid;

    public static void createBoard(int players){
	grid = new int[17][17];
	setBoard(players);
    }

    // Parameters: the number of players (2 or 4)
    // PostCondition: the board is empty and two or four players exist
    public static void setBoard(int players){
    	for(int i = 0; i < 17; i++){
    		for(int j = 0; j < 17; j++){
    			grid[i][j] = 0;
    		}
    	}
    	grid[0][8] = P1;
    	grid[16][8] = P2;
    	if(players == 4){
    		grid[8][0] = P3;
    		grid[8][16] = P4;
		}
    }

    // Parameters: character to represent the direction moved and an int
    //    to show which player is moving
    // PostCondition: the piece is moved if it was possible
    public static void movePiece(char direction, int Player){
	//int[] here = {2,2};//playerPlace(Player);
	int []here = playerPlace(Player);
	int row = here[0];
	int column = here[1];
	if(direction == 'N'){ // TODO: check for edge of board
		
	    if(grid[row-1][column] != WALL){ // TODO: check pawn collision
		grid[row][column] = 0;
		grid[row-2][column] = Player;
	    }
	}
	if(direction == 'S'){
	    if(grid[row+1][column] != WALL){
		grid[row][column] = 0;
		grid[row+2][column] = Player;
	    }
	}
	if(direction == 'E'){
	    if(grid[row][column+1] != WALL){
		grid[row][column] = 0;
		grid[row][column+2] = Player;
	    }
	}
	if(direction == 'W'){
	    if(grid[row][column-1] != WALL){
		grid[row][column] = 0;
		grid[row][column-2] = Player;
	    }
	}
    }

    // Parameters: the player being searched for
    // Returns: the x/y location of the Player
    public static int[] playerPlace(int Player)
    {
		int[] location = new int[2];
		for(int row = 0; row < 17; row++)
		{
		    for(int column = 0; column < 17; column++)
		    {
		    	
		    	if (grid[row][column] != 0) {
		    	}
		    	if(grid[row][column] == Player)
		    	{
		    		location[0] = row;
		    		location[1] = column;
		    		//break;
		    	}
		    }
		}
		return location;
    }

    // Returns: if any player is in their winning row
    public static boolean winCheck(){
	for(int i = 0; i < 17; i += 2){
	    if(grid[16][i] == P1)
		return true;
	    if(grid[0][i] == P2)
		return true;
	    if(grid[i][16] == P3)
		return true;
	    if(grid[i][0] == P4)
		return true;
	}
	return false;
    }
    
    //This method is for testing purposes only
    public static void moveToWin(){
    	grid[16][0] = P1;
    }

    // Returns: if each player can still reach their winning row
    public static boolean canWin(){
	for(int i = 0; i < 4; i++){
	    boolean open = doSearch();
	    if(open == false)
		return false;
	}
	return true;
    }

    // Paramters: an int representing a player
    // Returns: if the player can reach their win state
    private static boolean doSearch(){
	return true;
    }

}
