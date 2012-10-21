
/**
 * Tim Simmons
 * teamOrangeBeard
 */
package src.ui;

import java.util.*;

import javax.swing.Icon;

public class Board {

	final int NUMPLAY;
	final int WALL = 5;
	int[][] grid;
	static HashMap<Integer, Icon> map = new HashMap<Integer, Icon>();

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
		int[][] tempGrid = new int[17][17];
		for(int i = 0; i < 17; i++){
			for(int j = 0; j < 17; j++){
				tempGrid[i][j] = b.grid[i][j];
			}
		}
		this.grid = tempGrid;
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

	// Returns: the board in String form
	public String toString(){
		String s = "";
		for(int i = 0; i < 17; i++){
			for(int j = 0; j < 17; j++){
				s += (grid[j][i] + " ");
			}
			s += "\n";
		}
		return s;
	}
		
	// Parameters: the player being searched for
	// Returns: the x/y location of the Player
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

	// Parameters: character to represent the direction moved and an int
	//    to show which player is moving
	// PostCondition: the piece is moved if it was possible
	public void movePieceBoard(char direction, int Player){
		int[] here = playerPlace(Player);
		int col = here[0];
		int row = here[1];
				
		map.put(1, GameBoardWithButtons.playerOne);
		map.put(2, GameBoardWithButtons.playerTwo);
		map.put(3, GameBoardWithButtons.playerThree);
		map.put(4, GameBoardWithButtons.playerFour);
		
		if(direction == 'N'){
			grid[col][row] = 0;
			grid[col][row-2] = Player;
			BoardButton.map.get("B" + col/2 + row/2).setIcon(GameBoardWithButtons.defaultIcon);
			BoardButton.map.get("B" + col/2 + (row-2)/2).setIcon(map.get(Player));
			BoardButton.map.get("B" + col/2 + (row/2)).setPlayerPresent(false, col/2, row/2);
			BoardButton.map.get("B" + col/2 + (row-2)/2).setPlayerPresent(true, col/2, (row-2)/2);
		}
		if(direction == 'S'){
			grid[col][row] = 0;
			grid[col][row+2] = Player;
			BoardButton.map.get("B" + col/2 + row/2).setIcon(GameBoardWithButtons.defaultIcon);
			BoardButton.map.get("B" + col/2 + (row+2)/2).setIcon(map.get(Player));
			BoardButton.map.get("B" + col/2 + (row/2)).setPlayerPresent(false, col/2, row/2);
			BoardButton.map.get("B" + col/2 + (row+2)/2).setPlayerPresent(true, col/2, (row+2)/2);
		}
		if(direction == 'E'){
			grid[col][row] = 0;
			grid[col+2][row] = Player;
			BoardButton.map.get("B" + col/2 + row/2).setIcon(GameBoardWithButtons.defaultIcon);
			BoardButton.map.get("B" + (col+2)/2 + row/2).setIcon(map.get(Player));
			BoardButton.map.get("B" + col/2 + (row/2)).setPlayerPresent(false, col/2, row/2);
			BoardButton.map.get("B" + (col +2)/2 + row/2).setPlayerPresent(true, (col+2)/2, (row)/2);
		}
		if(direction == 'W'){
			grid[col][row] = 0;
			grid[col-2][row] = Player;
			BoardButton.map.get("B" + col/2 + row/2).setIcon(GameBoardWithButtons.defaultIcon);
			BoardButton.map.get("B" + (col-2)/2 + row/2).setIcon(map.get(Player));
			BoardButton.map.get("B" + col/2 + (row/2)).setPlayerPresent(false, col/2, row/2);
			BoardButton.map.get("B" + (col -2)/2 + row/2).setPlayerPresent(true, (col-2)/2, (row)/2);
		}
	}

	// Parameters: character to represent the direction moved and an int
	//    to show which player is moving
	// Returns: if the move is legal
	public boolean canMovePiece(char direction, int Player){
		//System.out.println("DIRECTION: "+ direction);
		int[] here = playerPlace(Player);
		int col = here[0];
		int row = here[1];
		if(direction == 'X')
			return false;
		if(direction == 'N'){
			if((here[1] == 0) || (grid[col][row-1] == 5))
				return false;
		}
		if(direction == 'S'){
			if((here[1] == 16) || (grid[col][row+1] == 5))
				return false;
		}
		if(direction == 'E'){
			if((here[0] == 16) || (grid[col+1][row] == 5))
				return false;
		}
		if(direction == 'W'){
			if((here[0] == 0) || (grid[col-1][row] == 5))
				return false;
		}
		return true;
	}

	// Parameters: an int[3] where the first and second numbers give the
	//    center of the new wall and the third number gives the
	//    direction the wall will face
	// PostCondition: a new wall is placed
	public void placeWallBoard(int[] theWall){
		int col = 2*theWall[0] + 1;
		int row = 2*theWall[1] + 1;
		if(theWall[2] == 0){
			grid[col][row] = WALL;
			grid[col-1][row] = WALL;
			grid[col+1][row] = WALL;
		}else{
			grid[col][row] = WALL;
			grid[col][row-1] = WALL;
			grid[col][row+1] = WALL;
		}
	}

	// Parameters: an int[3] where the first and second numbers give the
	//    center of the new wall and the third number gives the
	//    direction the wall will face
	// Returns: if the wall can be placed there
	public boolean canPlaceWall(int[] theWall){
		int col = 2*theWall[0] + 1;
		int row = 2*theWall[1] + 1;
		if(grid[col][row] != 0)
			return false;
		if(theWall[2]==0 && (grid[col-1][row]!=0 || grid[col+1][row]!=0))
			return false;
		if(theWall[2]==1 && (grid[col][row-1]!=0 || grid[col][row+1]!=0))
			return false;
		Board tempB = new Board(this);		
		tempB.placeWallBoard(theWall);
		if(!tempB.canWin())
		    return false;
		return true;
	}

	// Parameters: character to represent the direction moved and an int
	//    to show which player is moving
	// Returns: if moving that direction causes a collision with a player
	public boolean pieceCollision(char direction, int Player){
		int[] here = playerPlace(Player);
		int col = here[0];
		int row = here[1];
		if(direction == 'N' && grid[col][row-2] != 0)
			return true;
		if(direction == 'S' && grid[col][row+2] != 0)
			return true;
		if(direction == 'W' && grid[col-2][row] != 0)
			return true;
		if(direction == 'E' && grid[col+2][row] != 0)
			return true;
		return false;
	}

	// Returns: if each player can still reach their winning col
	public boolean canWin(){
		for(int i = 1; i < NUMPLAY + 1; i++){
			int[] nextMove = doSearch(i);
			if(nextMove[0] == -1)
				return false;
		}
		return true;
	}

	// Returns: if any player is in their winning col
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
	// Returns: if the player is in their winning col/row
	public boolean winCheck(int Player, int[] place){
		int col = place[0];
		int row = place[1];
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
		int col = location[0];
		int row = location[1];
		if((col != 0) && (this.grid[col-1][row] != WALL)){
			int[] next1 = new int[4]; 
			next1[0] = col-2;
			next1[1] = row;
			next1[2] = col-2;
			next1[3] = row;
			children.add(next1);
		}
		if((col != 16) && (this.grid[col+1][row] != WALL)){
			int[] next2 = new int[4];
			next2[0] = col+2;
			next2[1] = row;
			next2[2] = col+2;
			next2[3] = row;
			children.add(next2);
		}
		if((row != 0) && (this.grid[col][row-1] != WALL)){
			int[] next3 = new int[4];
			next3[0] = col;
			next3[1] = row-2;
			next3[2] = col;
			next3[3] = row-2;
			children.add(next3);
		}
		if((row != 16) && (this.grid[col][row+1] != WALL)){
			int[] next4 = new int[4];
			next4[0] = col;
			next4[1] = row+2;
			next4[2] = col;
			next4[3] = row+2;
			children.add(next4);
		}

		return children;
	}

	// Parameters: a location on the board
	// Returns: all adjacent locations that are not blocked by walls
	public ArrayList<int[]> getChildren(int[] place){

		ArrayList<int[]> children = new ArrayList<int[]>();
		int col = place[0];
		int row = place[1];
		if((col != 0) && (this.grid[col-1][row] != WALL)){
			int[] next1 = new int[4];
			next1[0] = col-2;
			next1[1] = row;
			next1[2] = place[2];
			next1[3] = place[3];
			children.add(next1);
		}
		if((col != 16) && (this.grid[col+1][row] != WALL)){
			int[] next2 = new int[4];
			next2[0] = col+2;
			next2[1] = row;
			next2[2] = place[2];
			next2[3] = place[3];
			children.add(next2);
		}
		if((row != 0) && (this.grid[col][row-1] != WALL)){
			int[] next3 = new int[4];
			next3[0] = col;
			next3[1] = row-2;
			next3[2] = place[2];
			next3[3] = place[3];
			children.add(next3);
		}
		if((row != 16) && (this.grid[col][row+1] != WALL)){
			int[] next4 = new int[4];
			next4[0] = col;
			next4[1] = row+2;
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