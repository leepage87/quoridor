
/**
 * Tim Simmons
 * teamOrangeBeard
 */
package src.ui;

import java.util.*;

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
    }

    // Parameters: a board
    // Creates: a board object that is a duplicate of the board given
    public Board(Board b){
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
        grid[8][0] = 1;
        grid[8][16] = 2;
        if(players == 4){
        	grid[0][8] = 3;
        	grid[16][8] = 4;
        }
    }

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
	}else{
	    grid[row][col] = WALL;
	    grid[row][col-1] = WALL;
	    grid[row][col+1] = WALL;
	}
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

    // Returns: if each player can still reach their winning row
    public boolean canWin(){
	for(int i = 1; i < NUMPLAY + 1; i++){
	    int[] nextMove = doSearch(i);
	    if(nextMove[0] == -1)
		return false;
	}
	return true;
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
    public boolean winCheck(int Player, int[] place){
	int row = place[1];
	int col = place[0];
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

    // Parameters: an int representing a player
    // Returns: the best position to move to next
    public int[] bestMove(int Player){
	return doSearch(Player);
    }

    // Parameters: an int representing a player
    // Returns: the best move, or -1/-1 if winning is impossible
    public int[] doSearch(int Player){
        ArrayList<int[]> checked = new ArrayList<int[]>();
        int[] location = new int[4];
        int[] startPlace = playerPlace(Player);
        location[0] = startPlace[0];
        location[1] = startPlace[1];
        location[2] = -1;
        location[3] = -1;
        int [] place = new int[4];
        Queue<int[]> toSearch = new LinkedList<int[]>();
        ArrayList<int[]> firstChildren = getFirstChildren(location);
        for(int j = 0; j < firstChildren.size(); j++){
            toSearch.add(firstChildren.get(j));
            checked.add(firstChildren.get(j));
        }
        boolean won = false;
        while(!won && (toSearch.size()>0)){
            place = toSearch.poll();
            int[] p1 = new int[2];
            p1[0] = place[0];
            p1[1] = place[1];
            if(winCheck(Player, p1)){
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
    public ArrayList<int[]> getFirstChildren(int[] location){
        ArrayList<int[]> children = new ArrayList<int[]>();
        int row = location[0];
        int col = location[1];
        if((row != 0) && (this.grid[row-1][col] != WALL)){
        	int[] next1 = new int[4]; 
            next1[0] = row-2;
            next1[1] = col;
            next1[2] = row-2;
            next1[3] = col;
            children.add(next1);
        }
        if((row != 16) && (this.grid[row+1][col] != WALL)){
            int[] next2 = new int[4];
        	next2[0] = row+2;
            next2[1] = col;
            next2[2] = row+2;
            next2[3] = col;
            children.add(next2);
        }
        if((col != 0) && (this.grid[row][col-1] != WALL)){
            int[] next3 = new int[4];
        	next3[0] = row;
            next3[1] = col-2;
            next3[2] = row;
            next3[3] = col-2;
            children.add(next3);
        }
        if((col != 16) && (this.grid[row][col+1] != WALL)){
        	int[] next4 = new int[4];
            next4[0] = row;
            next4[1] = col+2;
            next4[2] = row;
            next4[3] = col+2;
            children.add(next4);
        }
        
        return children;
    }

    // Parameters: a location on the board
    // Returns: all adjacent locations that are not blocked by walls
    public ArrayList<int[]> getChildren(int[] place){

        ArrayList<int[]> children = new ArrayList<int[]>();
        int row = place[0];
        int col = place[1];
        if((row != 0) && (this.grid[row-1][col] != WALL)){
        	int[] next1 = new int[4];
            next1[0] = row-2;
            next1[1] = col;
            next1[2] = place[2];
            next1[3] = place[3];
            children.add(next1);
        }
        if((row != 16) && (this.grid[row+1][col] != WALL)){
        	int[] next2 = new int[4];
            next2[0] = row+2;
            next2[1] = col;
            next2[2] = place[2];
            next2[3] = place[3];
            children.add(next2);
        }
        if((col != 0) && (this.grid[row][col-1] != WALL)){
        	int[] next3 = new int[4];
            next3[0] = row;
            next3[1] = col-2;
            next3[2] = place[2];
            next3[3] = place[3];
            children.add(next3);
        }
        if((col != 16) && (this.grid[row][col+1] != WALL)){
        	int[] next4 = new int[4];
            next4[0] = row;
            next4[1] = col+2;
            next4[2] = place[2];
            next4[3] = place[3];
            children.add(next4);
        }
        return children;
    }

    // Parameters: a list of all locations checked, the current location
    // Returns: if the current location has already been checked
    public boolean hasSeen(ArrayList<int[]> checked, int[] child){
	for(int i = checked.size()-1; i > 0; i--){
		int[] next = checked.get(i);
		if(child[0]==next[0] && child[1]==next[1])
			return true;
	}
	return false;
    }

////////////////////////////////////////////////////////////////////////////////

    //Testing purposes only
    public void setWall(){
    	grid[1][8]=5;
    }

    public void placePiece(){
    	grid[2][8] = 2;
    }

    public void moveToWin(int player){
    	if(player == 1){
    		grid[8][16] = 1;
    		grid[8][0]=0;
    	}else if(player == 2){
    		grid[8][0] = 2;
    		grid[8][16]=0;
    	}else if(player == 3){
    		grid[16][8] = 3;
    		grid[0][8]=0;
    	}else if(player == 4){
    		grid[0][8] = 4;
    		grid[16][8]=0;
    	}else{
    		System.out.println("Error");
    	}
    }

    public void placeTestWall(){
    	grid[1][9] = 5;
    }

}