package src.ui;

import javax.swing.JOptionPane;

/**
 * Tim, Lee, Sara, Jon
 * teamOrangeBeard
 */

public class PlayQuor{

	public static boolean clicked = false;
	public static String nextMove = "";
	public static String oldMove = "";
	public static int turn;
	private static int[] pieceHolder = new int[3];
	private static int[] playerWalls = new int[4];
	public static int breaker = 0;

	public static void main(String[] args) throws InterruptedException{
		String[] options = {"Two player game", "Four player game","Never mind, I'm done playing today."};
		GameBoardWithButtons gui = null;

		while (true){
			int n = JOptionPane.showOptionDialog(GameBoardWithButtons.contentPane, 
					"How many players want to play today?","Welcome to Quoridor!",
					JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
			int numPlay = 0;
			if (n == 0)
				numPlay = 2;
			else if (n == 1)
				numPlay = 4;
			else if (n == 2)
				System.exit(0);
			Board b = new Board(numPlay);
			for(int i = 0; i < numPlay; i++)
				playerWalls[i] = 20/numPlay;
			if (gui != null)
				gui.dispose();
			gui = new GameBoardWithButtons(b, numPlay);
			// Create/assign AI to a number of Players
			breaker = 0;
			boolean won = false;
			turn = 0;
			while(breaker == 0){
				turn = (turn%numPlay) + 1;
				System.out.println("TURN: " + turn);
				GameBoardWithButtons.whoseTurn.setText("It is player " + turn + "'s turn.");
				for (int i = 0; i < numPlay; i++)
					GameBoardWithButtons.pWalls.get(i).setText("P" + (i+1) + ": " + playerWalls[i] + " walls");
				System.out.println(b);
				boolean fairMove = false;
				while(!fairMove)
					fairMove = takeTurn(b, false);
				if (breaker == 2)
					break;
				won = b.haveWon();
				if (won)
					breaker = 1;
			}
			System.out.println(b);
			if (breaker == 1)
				JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Player " + turn + " Won!");
		}

	}	
	


	public static boolean takeTurn(Board b, boolean extraMove) throws InterruptedException{
		/*
	  Get placeWall/movePiece from Player/AI, in int[] form
	      movePiece: gives int to get the char from
	      placeWall: gives int/int/int (row, column, direction)
		 */
		while (!clicked){
			if (breaker == 2)
			{
				System.out.println("DISFHSD");
				return true;
			}
		}
		Thread.sleep(0);
		if (nextMove.charAt(0) == 'B') {//its a player move
			boolean getOut = false;
			for (int col = 0; col < 17; col+=2)
			{
				for (int row = 0; row < 17; row +=2)
				{
					System.out.print(b.grid[col][row] + " ");
					if (b.grid[col][row] == turn)
					{
						oldMove = "" + col/2 + row/2;
						int oldCol = (int)(oldMove.charAt(0) -'0');
						int oldRow = (int)(oldMove.charAt(1)-'0');
						int newCol = (int)(nextMove.charAt(1) - '0');
						int newRow = (int)(nextMove.charAt(2) - '0');
						char direction = getDirection(oldCol, oldRow, newCol, newRow);
						if(!movePiecePQ(b, direction, extraMove)){
							clicked = false;
							return false;
						}
						getOut = true;
					}
					if(getOut)
						break;
				}
				if(getOut)
					break;
			}
		}else if (!extraMove){//its a wall
			int gridCol = (int)(nextMove.charAt(0) - '0')-1;
			int gridRow = (int)(nextMove.charAt(1) - '0')-1;
			int[] theWall = {gridCol, gridRow, 0};
			boolean fairWall = false;

			if (nextMove.charAt(2) == 'H'){
				fairWall = placeWallPQ(b, theWall);
			}else{//its a vertical wall
				theWall[2] = 1;
				fairWall = placeWallPQ(b, theWall);
			}
			if (!fairWall)
			{
				JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Illegal Wall");
				clicked = false;
				return false;
			}

		}
		System.out.println(b);
		clicked = false;
		return true;

	}

	private static char getDirection(int oldCol, int oldRow, int newCol,
			int newRow) {
		char direction = 'X';
		if(newCol == oldCol -1 && oldRow == newRow)
			direction = 'W';
		else if(newCol == oldCol +1 && oldRow == newRow)
			direction = 'E';
		else if(oldCol == newCol && newRow == oldRow -1)
			direction = 'N';
		else if(oldCol == newCol && newRow == oldRow +1)
			direction = 'S';
		return direction;
	}

	// Parameters: the board, the player whose turn it is, the direction
	//    that the player chose to move
	// PostCondition: the player's piece is moved, if it was legal
	public static boolean movePiecePQ(Board b, char direction, boolean extraMove) throws InterruptedException{
		if(b.canMovePiece(direction, turn)){
			if(!b.pieceCollision(direction, turn)){
				b.movePieceBoard(direction, turn);
			}else{
				return doubleMove(b, direction, extraMove);
			}
			return true;
		}
		else {
			//JOptionPane.showMessageDialog(GameBoardWithButtons.contentPane, "Illegal Move");
			return false;
		}
	}

	// Parameters: the board, the player whose turn it is, and an
	//    int[] containing the center location of a new wall and
	//    an int determining if it is horizontal or vertical
	// PostCondition: the wall is placed, if it was legal
	public static boolean placeWallPQ(Board b, int[] theWall){
		String wallName;
		for(int i = 0; i < theWall.length; i++)
			if ((playerWalls[turn-1] > 0) && b.canPlaceWall(theWall))
			{
				b.placeWallBoard(theWall);
				playerWalls[turn-1]--;
				if (theWall[2] == 0)
					wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "H";
				else//its a 1, meaning vertical
					wallName = "" + (theWall[0]+1) + (theWall[1]+1) + "V";    
				BoardWall.map.get(wallName).setWall();
				BoardWall.map.get(wallName).nextWall().setWall();
				return true;
			}
			else
			{
				//System.out.println("WTF");
				return false;
			}
		return true;
	}

	// Parameters: the board, the player whose turn it is, the direction
	//    that the player chose to move (which is onto another player)
	// PostCondition: the player's piece is moved and he moves again
	public static boolean doubleMove(Board b, char direction, boolean extraMove) throws InterruptedException{
		int[] startSpot = b.playerPlace(turn);
		int col = startSpot[0];
		int row = startSpot[1];
		int otherPlayer;
		if(direction == 'N')
			otherPlayer = b.grid[col][row-2];
		else if(direction == 'S')
			otherPlayer = b.grid[col][row+2];
		else if(direction == 'E')
			otherPlayer = b.grid[col+2][row];
		else otherPlayer = b.grid[col-2][row];
		int[] spot = b.playerPlace(otherPlayer);
		b.movePieceBoard(direction, turn);
		if(extraMove){
			BoardButton.map.get("B"+pieceHolder[0]/2+pieceHolder[1]/2).setIcon(Board.map.get(pieceHolder[2]));
			b.grid[pieceHolder[0]][pieceHolder[1]] = pieceHolder[2];
		}
		pieceHolder[0] = spot[0];
		pieceHolder[1] = spot[1];
		pieceHolder[2] = otherPlayer;
		while(b.grid[spot[0]][spot[1]] ==  turn)
			takeTurn(b, true);
		b.grid[spot[0]][spot[1]] = otherPlayer;
		BoardButton.map.get("B"+spot[0]/2+spot[1]/2).setIcon(Board.map.get(otherPlayer));
		if(b.grid[col][row] == turn)
			return false;
		return true;
	}


}