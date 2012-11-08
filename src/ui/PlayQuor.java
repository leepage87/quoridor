package src.ui;

/**
 * Tim Simmons
 * teamOrangeBeard
 */

public class PlayQuor{

	public static boolean clicked = false;
	public static String nextMove = "";
	public static String oldMove = "";
	public static int turn;
	public static void main(String[] args) throws InterruptedException{
		int numPlay = 4; // Number of Players
		Board b = new Board(numPlay);
		GameBoardWithButtons gui = new GameBoardWithButtons(b);
		// Create/assign AI to a number of Players
		boolean won = false;
		turn = 1;
		while(!won){
			takeTurn(b, turn, false);
			turn = (turn%numPlay) + 1;
			System.out.println(turn);
			won = b.haveWon();
		}
	}

	public static void takeTurn(Board b, int turn, boolean moveOnly) throws InterruptedException{
	    /*
	  Get placeWall/movePiece from Player/AI, in int[] form
	      movePiece: gives int to get the char from
	      placeWall: gives int/int/int (row, column, direction)
	    */
	    while (!clicked){
		;
	    }
	    Thread.sleep(0);
	    if (nextMove.charAt(0) == 'B') {//its a player move
		for (int col = 0; col < 17; col+=2){
		    for (int row = 0; row < 17; row +=2){
			if (b.grid[col][row] == turn){
			    oldMove = "" + col/2 + row/2;
			    int oldCol = (int)(oldMove.charAt(0) -'0');
			    int oldRow = (int)(oldMove.charAt(1)-'0');
			    int newCol = (int)(nextMove.charAt(1) - '0');
			    int newRow = (int)(nextMove.charAt(2) - '0');
			    System.out.println("OldMove is: " + oldMove);
			    System.out.println("oldCol " + oldCol + " oldRow " + oldRow + " newCol: "+newCol + " newRow: " + newRow);
			    if(oldCol > newCol && oldRow == newRow){
				System.out.println("WE GO w");
				movePiece(b, turn, 'W');
			    }if(oldCol < newCol && oldRow == newRow){
				System.out.println("WE GO E");
				movePiece(b, turn, 'E');
			    }if(oldCol == newCol && oldRow > newRow){
				System.out.println("WE GO N");
				movePiece(b, turn, 'N');
			    }if(oldCol == newCol && oldRow < newRow){
				System.out.println("WE GO S");
				movePiece(b, turn, 'S');
			    }
			    //b.grid[col][row] = 0;
			    System.out.println("TURN: " + turn);
			    BoardButton.setPlayerPresent(false, col, row);
			}
		    }
		}
	    }
	    for (int col = 0; col < 17; col ++) {
		for(int row = 0; row < 17; row++) {
		    System.out.print(b.grid[row][col] + " ");
		}
		System.out.println();
	    }
	    clicked = false;
	}

	// Parameters: the board, the player whose turn it is, the direction
	//    that the player chose to move
	// PostCondition: the player's piece is moved, if it was legal
	public static void movePiece(Board b, int turn, char direction) throws InterruptedException{
	    System.out.println("Hey we in PQ.movepiece");
	    if(b.canMovePiece(direction, turn)){
		System.out.println("pass 1st if");
		if(!b.pieceCollision(direction, turn)){
		    b.movePieceBoard(direction, turn);
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
		b.placeWallBoard(theWall);
	    }else turn--;
	}

	// Parameters: the board, the player whose turn it is, the direction
	//    that the player chose to move (which is onto another player)
	// PostCondition: the player's piece is moved and he moves again
	public static void doubleMove(Board b, int turn, char direction) throws InterruptedException{
	    int[] startSpot = b.playerPlace(turn);
	    int col = startSpot[0];
	    int row = startSpot[1];
	    int otherPlayer;
	    if(direction == 'N')
		otherPlayer = b.grid[col][row-1];
	    else if(direction == 'S')
		otherPlayer = b.grid[col][row+1];
	    else if(direction == 'E')
		otherPlayer = b.grid[col+1][row];
	    else otherPlayer = b.grid[col-1][row];
	    int[] spot = b.playerPlace(otherPlayer);
	    b.movePieceBoard(direction, turn);
	    while(b.grid[spot[0]][spot[1]] == turn){
		takeTurn(b, turn, true);
	    }
	    b.grid[spot[0]][spot[1]] = otherPlayer;
	}


}