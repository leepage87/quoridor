package src.ui;

/**
 * Tim Simmons
 * teamOrangeBeard
 */

public class PlayQuor{

    public static void main(String[] args){
	int numPlay = 4; // Number of Players
	Board b = new Board(numPlay);
	// Create/assign AI to a number of Players
	boolean won = false;
	int turn = 1;
	while(!won){
	    takeTurn(b, turn, false);
	    turn = (turn%numPlay) + 1;
	    won = b.haveWon();
	}
    }

    public static void takeTurn(Board b, int turn, boolean moveOnly){
	/*
	  Get placeWall/movePiece from Player/AI, in int[] form
	      movePiece: gives int to get the char from
	      placeWall: gives int/int/int (row, column, direction)
	*/

    }

    // Parameters: the board, the player whose turn it is, the direction
    //    that the player chose to move
    // PostCondition: the player's piece is moved, if it was legal
    public static void movePiece(Board b, int turn, char direction){
	if(!b.wallCollision(direction, turn)){
	    if(!b.pieceCollision(direction, turn)){
		b.movePiece(direction, turn);
	    }else{
	        doubleMove(b, turn, direction);
	    }
	}else turn--;
    }

    // Parameters: the board, the player whose turn it is, and an
    //    int[] containing the center location of a new wall and
    //    an int determining if it is horizontal or vertical
    // PostCondition: the wall is placed, if it was legal
    public static void placeWall(Board b, int turn, int[] theWall){
	if(b.canPlaceWall(theWall)){
	    b.placeWall(theWall);
	}else turn--;
    }

    // Parameters: the board, the player whose turn it is, the direction
    //    that the player chose to move (which is onto another player)
    // PostCondition: the player's piece is moved and he moves again
    public static void doubleMove(Board b, int turn, char direction){
	int[] startSpot = b.playerPlace(turn);
	int row = startSpot[0];
	int col = startSpot[1];
	int otherPlayer;
	if(direction == 'N')
	    otherPlayer = b.grid[row-1][col];
	else if(direction == 'S')
	    otherPlayer = b.grid[row+1][col];
	else if(direction == 'E')
	    otherPlayer = b.grid[row][col+1];
	else otherPlayer = b.grid[row][col-1];
	int[] spot = b.playerPlace(otherPlayer);
	boolean safe = false;
	b.movePiece(direction, turn);
	while(!safe){
	    takeTurn(b, turn, true);
	}
	b.grid[spot[0]][spot[1]] = otherPlayer;
    }


}