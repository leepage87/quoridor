package ui;
/**
 * Tim Simmons
 * teamOrangeBeard
 */

import java.util.*;
import java.io.*;

public class Board {

    final int NUMPLAY;
    final int WALL = 5;

    int[][] grid;

    // Parameters: the number of players (2 or 4)
    // Creates: a board object
    public Board(int players){
	this.grid = new int[17][17];
	setBoard(players);
	if(players == 4)
	    NUMPLAY = 4;
	else
	    NUMPLAY = 2;
	/*
	this.wallSpace = new int[8][8];
	this.vWalls = new int[9][8];
	this.hWalls = new int[8][9];
	fillAllWalls();
	*/
    }

    // Parameters: a board
    // Creates: a board object that is a duplicate of the board given
    public Board(Board b){
	this.grid = b.grid;
	this.NUMPLAY = b.NUMPLAY;
	/*
	this.wallSpace = b.wallSpace;
	this.vWalls = b.vWalls;
	this.hWalls = b.hWalls;
	*/
    }

    // Parameters: the number of players (2 or 4)
    // PostCondition: the board is empty and two or four players exist

    private void setBoard(int players){
	for(int i = 0; i < 17; i++){
	    for(int j = 0; j < 17; j++){
		grid[i][j] = 0;
	    }
	}
	grid[0][8] = 1;
	grid[16][8] = 2;
	if(players == 4){
	    grid[8][0] = 3;
	    grid[8][16] = 4;
	}
    }

    /*
    // PostCondition: wallSpace, vWalls, and hWalls are all full of zeros
    private void fillAllWalls(){
	for(int i = 0; i < 8; i++){
	    for(int j = 0; j < 8; j++){
		wallSpace[i][j] = 0;
		vWalls[i][j] = 0;
		hWalls[i][j] = 0;
	    }
	}
	for(int k = 0; k < 8; k++){
		vWalls[9][k] = 0;
		hWalls[k][9] = 0;
	}
    }
    */

    // Parameters: a board
    // Returns: if this board is identical to the other board
    public boolean equals(Board b){
	boolean check = true;
	for(int i = 0; i < 17; i++){
	    for(int j = 0; j < 17; j++){
		if(grid[i][j] != b.grid[i][j])
		    check = false;
	    }
	}
	return check;

    }

    // Parameters: character to represent the direction moved and an int
    //    to show which player is moving
    // PostCondition: the piece is moved if it was possible
    public void movePiece(char direction, int Player){
	int[] here = playerPlace(Player);
	int row = here[0];
	int column = here[1];
	if(direction == 'N'){ // TODO: check for edge of board
	    grid[row][column] = 0;
	    grid[row-2][column] = Player;
	}
	if(direction == 'S'){
	    grid[row][column] = 0;
	    grid[row+2][column] = Player;
	}
	if(direction == 'E'){
	    grid[row][column] = 0;
	    grid[row][column+2] = Player;
	}
	if(direction == 'W'){
	    grid[row][column] = 0;
	    grid[row][column-2] = Player;
	}
    }

    // Parameters: character to represent the direction moved and an int
    //    to show which player is moving
    // Returns: if moving that direction causes a collision with a wall
    public boolean wallCollision(char direction, int Player){
	int[] here = playerPlace(Player);
	int row = here[0];
	int column = here[1];
	if(direction == 'N' && grid[row-1][column] != 0)
	    return true;
	if(direction == 'S' && grid[row+1][column] != 0)
	    return true;
	if(direction == 'E' && grid[row][column+1] != 0)
	    return true;
	if(direction == 'W' && grid[row][column-1] != 0)
	    return true;
	return false;
    }

    // Parameters: character to represent the direction moved and an int
    //    to show which player is moving
    // Returns: if moving that direction causes a collision with a player
    public boolean pieceCollision(char direction, int Player){
	int[] here = playerPlace(Player);
	int row = here[0];
	int column = here[1];
	if(direction == 'N' && grid[row-2][column] != 0)
	    return true;
	if(direction == 'S' && grid[row+2][column] != 0)
	    return true;
	if(direction == 'E' && grid[row][column+2] != 0)
	    return true;
	if(direction == 'W' && grid[row][column-2] != 0)
	    return true;
	return false;
    }

    // Parameters: the player being searched for
    // Returns: the x/y location of the Player
    public int[] playerPlace(int Player){
	int[] location = new int[2];
	for(int row = 0; row < 17; row++){
	    for(int column = 0; column < 17; column++){
		if(grid[row][column] == Player){
		    location[0] = row;
		    location[1] = column;
		    break;
		}
	    }
	}
	return location;
    }

    // Returns: if any player is in their winning row
    public boolean haveWon(){
	for(int i = 1; i < NUMPLAY + 1; i++){
	    int[] place = playerPlace(i);
	    if(winCheck(i, place))
		return true;
	}
	return false;
    }

    // Parameters: an int representing a player and an int[] to show
    //    where on the board the player is
    // Returns: if the player is in their winning row/column
    private boolean winCheck(int Player, int[] place){
	int row = place[0];
	int col = place[1];
	if(Player==1 && row==16)
	    return true;
	if(Player==2 && row==0)
	    return true;
	if(Player==3 && col==16)
	    return true;
	if(Player==4 && col==0)
	    return true;
	return false;
    }

    // Parameters: an int[3] where the first and second numbers give the
    //    center of the new wall and the third number gives the
    //    direction the wall will face
    // Returns: if the wall can be placed there
    public boolean canPlaceWall(int[] theWall){
	int row = 2*theWall[0] + 1;
	int col = 2*theWall[1] + 1;
	if(grid[row][col] != 0)
	    return false;
	if(theWall[2]==0 && (grid[row-1][col]!=0 || grid[row+1][col]!=0))
	    return false;
	if(theWall[2]==1 && (grid[row][col-1]!=0 || grid[row][col+1]!=0))
	    return false;
	return true;
    }

    // Parameters: an int[3] where the first and second numbers give the
    //    center of the new wall and the third number gives the
    //    direction the wall will face
    // PostCondition: a new wall is placed
    public void placeWall(int[] theWall){
	int row = 2*theWall[0] + 1;
	int col = 2*theWall[1] + 1;
	if(theWall[2] == 0){
	    grid[row][col] = WALL;
	    grid[row-1][col] = WALL;
	    grid[row+1][col] = WALL;
	    // GUI mod?
	}else{
	    grid[row][col] = WALL;
	    grid[row][col-1] = WALL;
	    grid[row][col+1] = WALL;
	    // GUI mod?
	}
    }

    // Returns: if each player can still reach their winning row
    public boolean canWin(){
	for(int i = 1; i < NUMPLAY + 1; i++){
	    int[] nextMove = doSearch(i);
	    if(nextMove[0] == -1)
		return false;
	}
	return true;
    }

    // Parameters: an int representing a player
    // Returns: the best position to move to next
    public int[] bestMove(int Player){
	return doSearch(Player);
    }

    // Parameters: an int representing a player
    // Returns: the best move, or -1/-1 if winning is impossible
    private int[] doSearch(int Player){
	ArrayList<int[]> checked = new ArrayList<int[]>();
	int[] location = new int[4];
	int[] startPlace = playerPlace(Player);
	location[0] = startPlace[0];
	location[1] = startPlace[1];
	location[2] = -1;
	location[3] = -1;
	Queue<int[]> toSearch = new LinkedList<int[]>();
	firstChildren(location, toSearch, checked);
	boolean won = false;
	while(!won && (checked.size()>0)){
	    int[] place = toSearch.poll();
	    if(winCheck(Player, place)){
		location = place;
		won = true;
		break;
	    }
            ArrayList<int[]> children = getChildren(place);
	    for(int i = 0; i < children.size(); i++){
		if(!hasSeen(checked, children.get(i))){
		    toSearch.add(children.get(i));
		    checked.add(children.get(i));
		}
	    }
	}
	int[] answer = new int[2];
	answer[0] = location[2];
	answer[1] = location[3];
	return answer;
    }

    // Parameters: the starting location, the queue of places to search,
    //    and the ArrayList of locations checked
    // PostConditions: up to four children are created, each of which
    //    has a record of the first move made
    private void firstChildren(int[] location, Queue<int[]> toSearch,
			       ArrayList<int[]> checked){
	int row = location[0];
	int col = location[1];
	int[] next = location;
	if((row != 0) && (this.grid[row-1][col] != WALL)){
	    next[0] = row-2;
	    next[1] = col;
	    next[2] = row-2;
	    next[3] = col;
	    toSearch.add(next);
	    checked.add(next);
	}
	if((row != 16) && (this.grid[row+1][col] != WALL)){
	    next[0] = row+2;
	    next[1] = col;
	    next[2] = row+2;
	    next[3] = col;
	    toSearch.add(next);
	    checked.add(next);
	}
	if((col != 0) && (this.grid[row][col-1] != WALL)){
	    next[0] = row;
	    next[1] = col-2;
	    next[2] = row;
	    next[3] = col-2;
	    toSearch.add(next);
	    checked.add(next);
	}
	if((col != 16) && (this.grid[row][col+1] != WALL)){
	    next[0] = row;
	    next[1] = col+2;
	    next[2] = row;
	    next[3] = col+2;
	    toSearch.add(next);
	    checked.add(next);
	}
    }

    // Parameters: a location on the board
    // Returns: all adjacent locations that are not blocked by walls
    private ArrayList<int[]> getChildren(int[] place){
	ArrayList<int[]> children = new ArrayList<int[]>();
	int row = place[0];
	int col = place[1];
	int[] next = place;
	if((row != 0) && (this.grid[row-1][col] != WALL)){
	    next[0] = row-2;
	    next[1] = col;
	    children.add(next);
	}
	if((row != 16) && (this.grid[row+1][col] != WALL)){
	    next[0] = row+2;
	    next[1] = col;
	    children.add(next);
	}
	if((col != 0) && (this.grid[row][col-1] != WALL)){
	    next[0] = row;
	    next[1] = col-2;
	    children.add(next);
	}
	if((col != 16) && (this.grid[row][col+1] != WALL)){
	    next[0] = row;
	    next[1] = col+2;
	    children.add(next);
	}
	return children;
    }

    // Parameters: a list of all locations checked, the current location
    // Returns: if the current location has already been checked
    private boolean hasSeen(ArrayList<int[]> checked, int[] child){
	for(int i = checked.size()-1; i > 0; i--){
	    int[] next = checked.get(i);
	    if(child[0]==next[0] && child[1]==next[1])
		return true;
	}
	return false;
    }

}
