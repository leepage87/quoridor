
/**
 * Tim, Lee, Sarah, Jon
 * teamOrangeBeard
 */
package src.main;

import java.util.*;

import javax.swing.Icon;

import src.ui.BoardButton;
import src.ui.GameBoardWithButtons;
/** The back end. Contains a grid for the contents of the game board (both walls and tokens)
 * and methods for testing the board's possibilities. 
 * */
public class Board {

	public final int NUMPLAY; /** number of players*/
	public int[] playerWalls = new int[4]; /** tracks players per wall*/
	final int WALL = 5; /**how a wall is denoted on the grid*/
	public int[][] grid; /**the board array*/
	public static HashMap<Integer, Icon> map = new HashMap<Integer, Icon>(); /** determines which icon to paint when a tile is updated*/
	/**
	* @param players (2 or 4)
	* @return a board object
	*/
	public Board(int players){
		this.grid = new int[17][17];
		setBoard(players);
		if(players == 4)
			NUMPLAY = 4;
		else
			NUMPLAY = 2;
		for(int i = 0; i < NUMPLAY; i++)
			playerWalls[i] = 20/NUMPLAY;
		mapIcons();
	}

	public void setPlayerWalls(int[] playerWallss){
		playerWalls = playerWallss;
	}
	
	public int[] getPlayerWalls(){
		return playerWalls;
	}
	/**
	* @param b a board object that is a duplicate of the board given
	*/
	public Board(Board b){
		int[][] tempGrid = new int[17][17];
		for(int i = 0; i < 17; i++){
			for(int j = 0; j < 17; j++){
				tempGrid[i][j] = b.grid[i][j];
			}
		}
		this.grid = tempGrid;
		this.NUMPLAY = b.NUMPLAY;
		for(int i = 0; i < 4; i++)
			this.playerWalls[i] = b.playerWalls[i];
		mapIcons();
	}
	/**
	 * PostCondition: the board is empty and two or four players exist
	 * @param players (2 or 4) 
	 */
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
	/**
	* @param b a board
	* @return if this board is identical to the other board
	*/
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

	/**
	 * @return the board in String form
	 */
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
		
	/**
	 * @param Player the player being searched for
	 * @return the x/y location of the Player
	 */
	public int[] playerPlace(int Player){
		int[] location = new int[2];
		location[0] = -1;
		location[1] = -1;
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

	/**
	* @param direstion a character representing the direction moved 
	* @param place a location
	* @return if moving that direction from the location causes a collision with a player
	*/
	private boolean pieceCollision(char direction, int[] place) {
		int col = place[0];
		int row = place[1];
		if(direction == 'N' && row != 0 && grid[col][row-2] != 0)
			return true;
		if(direction == 'S' && row != 16 && grid[col][row+2] != 0)
			return true;
		if(direction == 'W' && col != 0 && grid[col-2][row] != 0)
			return true;
		if(direction == 'E' && col != 16 && grid[col+2][row] != 0)
			return true;
		return false;
	}
	
	/**
	* @param dest the location the AI wants to move to 
	* @param Player an int representing the player 
	* @return if that movement is legal
	*/
	public boolean aiCanMove(int[] dest, int Player){
		if(grid[dest[0]][dest[1]] != 0)
			return false;
		ArrayList<int[]> history = new ArrayList<int[]>();
		return aiCanMove(dest, Player, history);
	}
	/**
	* @param place location on the board
	* @param Player an int representing the player
	* @param history a list of locations already searched
	* @return if it is possible for the Player to move to the given location
	*/
	public boolean aiCanMove(int[] place, int Player, ArrayList<int[]> history){
		int[] home = playerPlace(Player);
		if((home[0] == place[0]-2) && (home[1] == place[1]) && (grid[place[0]-1][place[1]] != 5))
			return true;
		if((home[0] == place[0]+2) && (home[1] == place[1]) && (grid[place[0]+1][place[1]] != 5))
			return true;
		if((home[0] == place[0]) && (home[1] == place[1]+2) && (grid[place[0]][place[1]+1] != 5))
			return true;
		if((home[0] == place[0]) && (home[1] == place[1]-2) && (grid[place[0]][place[1]-1] != 5))
			return true;
		return aiDoubleMove(place, Player, history);	
	}
	
	/**
	* @param place the location on the board
	* @param Player an int representing the player
	* @param history a list of locations already searched
	* @return if it is possible for the Player to move to the given location
	*/
	private boolean aiDoubleMove(int[] place, int Player, ArrayList<int[]> history) {
		int col = place[0];
		int row = place[1];
		if(pieceCollision('N', place) && grid[col][row-1] != 5){
			int[] newPlace = new int[2];
			newPlace[0] = col;
			newPlace[1] = row-2;
			if(!hasBeen(history, newPlace)){
				ArrayList<int[]> newHistory = new ArrayList<int[]>(history);
				newHistory.add(place);
				return aiCanMove(newPlace, Player, newHistory);
			}
		}else if(pieceCollision('S', place) && grid[col][row+1] != 5){
			int[] newPlace = new int[2];
			newPlace[0] = col;
			newPlace[1] = row+2;
			if(!hasBeen(history, newPlace)){
				ArrayList<int[]> newHistory = new ArrayList<int[]>(history);
				newHistory.add(place);
				return aiCanMove(newPlace, Player, newHistory);
			}
		}else if(pieceCollision('E', place) && grid[col+1][row] != 5){
			int[] newPlace = new int[2];
			newPlace[0] = col+2;
			newPlace[1] = row;
			if(!hasBeen(history, newPlace)){
				ArrayList<int[]> newHistory = new ArrayList<int[]>(history);
				newHistory.add(place);
				return aiCanMove(newPlace, Player, newHistory);
			}
		}else if(pieceCollision('W', place) && grid[col-1][row] != 5){
			int[] newPlace = new int[2];
			newPlace[0] = col-2;
			newPlace[1] = row;
			if(!hasBeen(history, newPlace)){
				ArrayList<int[]> newHistory = new ArrayList<int[]>(history);
				newHistory.add(place);
				return aiCanMove(newPlace, Player, newHistory);
			}
		}
		return false;
	}
	
	/**
	* @param history a list of locations that have been searched
	* @param newPlace a location
	* @return if the location has already been searched
	*/
	private boolean hasBeen(ArrayList<int[]> history, int[] newPlace) {
		boolean seen = false;
		for(int i = 0; i < history.size(); i++){
			int[] next = history.get(i);
			if(newPlace[0] == next[0] && newPlace[1] == next[1]){
				seen = true;
				break;
			}
		}
		return seen;
	}
	
	/**
	 * PostCondition: the Player is moved to the destination
	 * @param dest a location the AI wants to move to 
	 * @param Player an int representing the player
	 */
	public void quickMove(int[] dest, int Player){
		int dCol = dest[0];
		int dRow = dest[1];
		int[] here = playerPlace(Player);
		int hCol = here[0];
		int hRow = here[1];
		grid[dCol][dRow] = Player;
		grid[hCol][hRow] = 0;
	}
	
	/**
	 * PostCondition: a new wall is placed
	 * @param theWall an int[3] where the first and second numbers give the
	 *    center of the new wall and the third number gives the
	 *    direction the wall will face
	 * @param player an int representing the player
	 */
	public void placeWallBoard(int[] theWall, int player){
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
		playerWalls[player-1]--;
	}
	/**
	* @param theWall an int[3] where the first and second numbers give the
	*    center of the new wall and the third number gives the
	*    direction the wall will face
	* @param player an int representing the player
	* @return if the wall can be placed there
	*/
	public boolean canPlaceWall(int[] theWall, int player){
		if(playerWalls[player-1] == 0)
			return false;
		int col = 2*theWall[0] + 1;
		int row = 2*theWall[1] + 1;
		if(grid[col][row] != 0)
			return false;
		if(theWall[2]==0 && (grid[col-1][row]!=0 || grid[col+1][row]!=0))
			return false;
		if(theWall[2]==1 && (grid[col][row-1]!=0 || grid[col][row+1]!=0))
			return false;
		Board tempB = new Board(this);		
		tempB.placeWallBoard(theWall, player);
		if(!tempB.canWin())
		    return false;
		return true;
	}
	/**
	* @return if each player can still reach their winning column
	*/
	public boolean canWin(){
		for(int i = 1; i < NUMPLAY + 1; i++){
			int[] nextMove = doSearch(i);
			if(nextMove[0] == -1)
				return false;
		}
		return true;
	}
	/**
	* @return if any player is in their winning column
	*/
	public boolean haveWon(){
		for(int i = 1; i < NUMPLAY + 1; i++){
			int[] place = playerPlace(i);
			if(winCheck(i, place))
				return true;
		}
		return false;
	}
	/**
	* @param Player an int representing a player 
	* @param place an int[] to show where on the board the player is
	* @return if the player is in their winning column/row
	*/
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
	/**
	* @param Player an int representing a player
	* @return the best position to move to next
	*/
	public int[] bestMove(int Player){
		return doSearch(Player);
	}
	/**
	* @param Player an int representing a player
	* @return the best move, or -1/-1 if winning is impossible
	*/
	public int[] doSearch(int Player){
		ArrayList<int[]> checked = new ArrayList<int[]>();
		int[] location = new int[5];
		int[] startPlace = playerPlace(Player);
		location[0] = startPlace[0];
		location[1] = startPlace[1];
		location[2] = -1;
		location[3] = -1;
		location[4] = 0;
		int [] place = new int[5];
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
		int[] answer = new int[3];
		answer[0] = location[2];
		answer[1] = location[3];
		answer[2] = location[4];
		return answer;
	}
	/**
	 * PostConditions: up to four children are created, each of which
	 *    has a record of the first move made
	 * @param location the starting location, the queue of places to search,
	 *    and the ArrayList of locations checked
	 */
	public ArrayList<int[]> getFirstChildren(int[] location){
		ArrayList<int[]> children = new ArrayList<int[]>();
		int col = location[0];
		int row = location[1];
		if((col != 0) && (this.grid[col-1][row] != WALL)){
			int[] next1 = new int[5]; 
			next1[0] = col-2;
			next1[1] = row;
			next1[2] = col-2;
			next1[3] = row;
			next1[4] = 1;
			children.add(next1);
		}
		if((col != 16) && (this.grid[col+1][row] != WALL)){
			int[] next2 = new int[5];
			next2[0] = col+2;
			next2[1] = row;
			next2[2] = col+2;
			next2[3] = row;
			next2[4] = 1;
			children.add(next2);
		}
		if((row != 0) && (this.grid[col][row-1] != WALL)){
			int[] next3 = new int[5];
			next3[0] = col;
			next3[1] = row-2;
			next3[2] = col;
			next3[3] = row-2;
			next3[4] = 1;
			children.add(next3);
		}
		if((row != 16) && (this.grid[col][row+1] != WALL)){
			int[] next4 = new int[5];
			next4[0] = col;
			next4[1] = row+2;
			next4[2] = col;
			next4[3] = row+2;
			next4[4] = 1;
			children.add(next4);
		}

		return children;
	}
	/**
	* @param place the location on the board
	* @return all adjacent locations that are not blocked by walls
	*/
	public ArrayList<int[]> getChildren(int[] place){

		ArrayList<int[]> children = new ArrayList<int[]>();
		int col = place[0];
		int row = place[1];
		if((col != 0) && (this.grid[col-1][row] != WALL)){
			int[] next1 = new int[5];
			next1[0] = col-2;
			next1[1] = row;
			next1[2] = place[2];
			next1[3] = place[3];
			next1[4] = place[4]+1;
			children.add(next1);
		}
		if((col != 16) && (this.grid[col+1][row] != WALL)){
			int[] next2 = new int[5];
			next2[0] = col+2;
			next2[1] = row;
			next2[2] = place[2];
			next2[3] = place[3];
			next2[4] = place[4]+1;
			children.add(next2);
		}
		if((row != 0) && (this.grid[col][row-1] != WALL)){
			int[] next3 = new int[5];
			next3[0] = col;
			next3[1] = row-2;
			next3[2] = place[2];
			next3[3] = place[3];
			next3[4] = place[4]+1;
			children.add(next3);
		}
		if((row != 16) && (this.grid[col][row+1] != WALL)){
			int[] next4 = new int[5];
			next4[0] = col;
			next4[1] = row+2;
			next4[2] = place[2];
			next4[3] = place[3];
			next4[4] = place[4]+1;
			children.add(next4);
		}
		return children;
	}
	/**
	* @param checked a list of all locations checked
	* @param child the current location
	* @return if the current location has already been checked
	*/
	public boolean hasSeen(ArrayList<int[]> checked, int[] child){
		for(int i = checked.size()-1; i > 0; i--){
			int[] next = checked.get(i);
			if(child[0]==next[0] && child[1]==next[1])
				return true;
		}
		return false;
	}
	
	public void mapIcons(){
		/** Adds the icons from GBWB to the map. Allows direct translation
		 * of turn number to icon. */
		map.put(0, GameBoardWithButtons.defaultIcon);
		map.put(1, GameBoardWithButtons.playerOne);
		map.put(2, GameBoardWithButtons.playerTwo);
		map.put(3, GameBoardWithButtons.playerThree);
		map.put(4, GameBoardWithButtons.playerFour);
		map.put(5, GameBoardWithButtons.legalMove);
	}
	/**
	*Testing Purposes Only
	*/
	public void setWall(){
		grid[1][8]=5;
	}

	/**
	 * Testing Purposes Only
	 */
	public void placePiece(){
		grid[2][8] = 2;
	}

	/**
	 * Testing Purposes Only
	 * @param player
	 */
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

	/**
	 * Testing Purposes Only
	 */
	public void placeTestWall(){
		grid[1][9] = 5;
	}

}
