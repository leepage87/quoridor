/**
 * Tim Simmons
 * teamOrangeBeard
 */

public class Board {

    final int P1 = 1;
    final int P2 = 2;
    final int P3 = 3;
    final int P4 = 4;
    final int WALL = 5;

    int[][] grid;

    public Board(int players){
	grid = new int[17][17];
	setBoard(players);
    }

    // Parameters: the number of players (2 or 4)
    // PostCondition: the board is empty and two or four players exist
    public void setBoard(int players){
	for(int i = 0; i < 17; i++){
	    for(int j = 0; j < 17; j++){
		graph[i][j] = 0;
	    }
	}
	graph[0][8] = P1;
	graph[16][8] = P2;
	if(players == 4){
	    graph[8][0] = P3;
	    graph[8][16] = P4;
	}
    }

    // Parameters: character to represent the direction moved and an int
    //    to show which player is moving
    // PostCondition: the piece is moved if it was possible
    public void movePiece(char direction, int Player){
	int[] here = playerPlace(Player);
	int row = here[0];
	int column = here[1];
	if(direction == 'N'){ // TODO: check for edge of board
	    if(graph[row-1][column] != WALL){ // TODO: check pawn collision
		graph[row][column] = 0;
		graph[row-2][column] = Player;
	    }
	}
	if(direction == 'S'){
	    if(graph[row+1][column] != WALL){
		graph[row][column] = 0;
		graph[row+2][column] = Player;
	    }
	}
	if(direction == 'E'){
	    if(graph[row][column+1] != WALL){
		graph[row][column] = 0;
		graph[row][column+2] = Player;
	    }
	}
	if(direction == 'W'){
	    if(graph[row][column-1] != WALL){
		graph[row][column] = 0;
		graph[row][column-2] = Player;
	    }
	}
    }

    // Parameters: the player being searched for
    // Returns: the x/y location of the Player
    public int[] playerPlace(int Player){
	int[] location = new int[2];
	for(int row = 0; row < 17; row++){
	    for(int column = 0; column < 17; column++){
		if(graph[i][j] == Player){
		    location[0] = row;
		    location[1] = column;
		    break;
		}
	    }
	}
	return location;
    }

    // Returns: if any player is in their winning row
    public boolean winCheck(){
	for(int i = 0; i < 17; i += 2){
	    if(graph[16][i] == P1)
		return true;
	    if(graph[0][i] == P2)
		return true;
	    if(graph[i][16] == P3)
		return true;
	    if(graph[i][0] == P4)
		return true;
	}
	return false;
    }

    // Returns: if each player can still reach their winning row
    public boolean canWin(){
	for(int i = 0; i < 4; i++){
	    boolean open = doSearch(i);
	    if(open == false)
		return false;
	}
	return true;
    }

    // Paramters: an int representing a player
    // Returns: if the player can reach their win state
    private boolean doSearch(){
	return true;
    }

}
